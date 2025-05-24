package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.analysissessionresult

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.neotelemetrixgdscunand.kakaoxpert.R
import com.neotelemetrixgdscunand.kakaoxpert.domain.common.CocoaAnalysisError
import com.neotelemetrixgdscunand.kakaoxpert.domain.common.Result
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.BoundingBox
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.CocoaDisease
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.DetectedCocoa
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.PriceAnalysisOverview
import com.neotelemetrixgdscunand.kakaoxpert.domain.usecase.AnalysisCocoaUseCase
import com.neotelemetrixgdscunand.kakaoxpert.domain.usecase.GetCocoaAnalysisSessionUseCase
import com.neotelemetrixgdscunand.kakaoxpert.presentation.dui.AnalysisSessionDui
import com.neotelemetrixgdscunand.kakaoxpert.presentation.dui.DiagnosisResultOverviewDui
import com.neotelemetrixgdscunand.kakaoxpert.presentation.dui.PriceAnalysisOverviewDui
import com.neotelemetrixgdscunand.kakaoxpert.presentation.mapper.DuiMapper
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.Navigation
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.analysissessionresult.util.getBoundingBoxWithItsNameAsTheLabel
import com.neotelemetrixgdscunand.kakaoxpert.presentation.utils.UIText
import com.neotelemetrixgdscunand.kakaoxpert.presentation.utils.deepCopy
import com.neotelemetrixgdscunand.kakaoxpert.presentation.utils.toErrorUIText
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException
import kotlin.math.roundToInt


@HiltViewModel
class AnalysisResultViewModel @Inject constructor(
    private val analysisCocoaUseCase: AnalysisCocoaUseCase,
    private val getCocoaAnalysisSessionUseCase: GetCocoaAnalysisSessionUseCase,
    private val savedStateHandle: SavedStateHandle,
    private val duiMapper: DuiMapper,
    @ApplicationContext private val applicationContext: Context
) : ViewModel() {

    // Backup new diagnosis session id that just has been saved, in case process death happens
    private var backupNewAnalysisSessionIdThatJustSaved: Int?
        get() = savedStateHandle[BACKUP_NEW_ANALYSIS_SESSION_ID_THAT_JUST_SAVED]
        set(value) {
            savedStateHandle[BACKUP_NEW_ANALYSIS_SESSION_ID_THAT_JUST_SAVED] = value
        }

    //May be Modified cause of multiplier change
    private val invalidSessionId = -1
    private val _presentedAnalysisSessionDui =
        MutableStateFlow(AnalysisSessionDui(id = invalidSessionId))

    @Volatile
    private var unmodifiedAnalysisSessionDui: AnalysisSessionDui? = null

    private val _uiState = MutableStateFlow(AnalysisResultUIState())
    val uiState = _uiState.asStateFlow()

    private val _event = Channel<AnalysisResultUIEvent>()
    val event = _event
        .receiveAsFlow()

    private var detectImageJob: Job? = null

    private val extras = savedStateHandle.toRoute<Navigation.DiagnosisResult>()

    private val initialAssumedCocoaAverageWeight = 0.2f //in KG

    fun onChangeSelectedTab(isDiagnosisTabSelected: Boolean) {
        if (isDiagnosisTabSelected) {
            onCocoaAverageWeightChanged(initialAssumedCocoaAverageWeight.toString())
            _multiplier.update { 1f }
        }

        _uiState.update {
            it.copy(
                isDiagnosisTabSelected = isDiagnosisTabSelected
            )
        }
    }

    private val allowedCharacterPattern = "^[0-9]*[.,]?[0-9]*\$".toRegex()


    private val _cocoaAverageWeightInput = MutableStateFlow("$initialAssumedCocoaAverageWeight")
    val cocoaAverageWeightInput = _cocoaAverageWeightInput.asStateFlow()

    private val _multiplier = MutableStateFlow(1f)

    private var calculateMultiplierJob: Job? = null

    private val debounceDuration = 2000L

    fun onCocoaAverageWeightChanged(value: String) {
        if (!allowedCharacterPattern.matches(value) || value.length > 3) return

        _cocoaAverageWeightInput.update { value }

        calculateMultiplierJob?.cancel()
        calculateMultiplierJob = viewModelScope.launch {
            delay(debounceDuration)

            try {
                if (value.isBlank()) {
                    _event.send(
                        AnalysisResultUIEvent.OnInputCocoaAverageWeightInvalid(
                            UIText.StringResource(R.string.input_berat_rata_rata_kakao_tidak_boleh_kosong)
                        )
                    )

                    withContext(NonCancellable) {
                        _cocoaAverageWeightInput.update { "$initialAssumedCocoaAverageWeight" }
                    }
                    return@launch
                }

                val finalValue = if (value.contains(",")) {
                    value.replace(",", ".")
                } else value

                val isValueAFloat = try {
                    finalValue.toFloat()
                    true
                } catch (e: NumberFormatException) {
                    false
                }

                if (!isValueAFloat) {
                    _event.send(
                        AnalysisResultUIEvent.OnInputCocoaAverageWeightInvalid(
                            UIText.StringResource(R.string.input_berat_rata_rata_kakao_tidak_valid)
                        )
                    )

                    withContext(NonCancellable) {
                        _cocoaAverageWeightInput.update { "$initialAssumedCocoaAverageWeight" }
                    }
                    return@launch
                }

                if (finalValue.toFloat() > 5f) {
                    _event.send(
                        AnalysisResultUIEvent.OnInputCocoaAverageWeightInvalid(
                            UIText.StringResource(R.string.input_berat_rata_rata_kakao_tidak_boleh_melebihi_5kg)
                        )
                    )

                    withContext(NonCancellable) {
                        _cocoaAverageWeightInput.update { "$initialAssumedCocoaAverageWeight" }
                    }
                    return@launch
                }

                _multiplier.update { finalValue.toFloat() / initialAssumedCocoaAverageWeight }
                _cocoaAverageWeightInput.update { finalValue }

            } catch (e: Exception) {
                if (e is CancellationException) throw e

                withContext(NonCancellable) {
                    _cocoaAverageWeightInput.update { "$initialAssumedCocoaAverageWeight" }
                }

            }
        }

    }

    private val getGroupedDetectedDiseaseToDetectedCocoas =
        { analysisSessionDui: AnalysisSessionDui ->
            val map = mutableMapOf<CocoaDisease, ImmutableList<DetectedCocoa>>()
            analysisSessionDui.detectedCocoas.groupBy {
                it.disease
            }.map {
                val (cocoaDisease, list) = it.toPair()
                map[cocoaDisease] = list.toImmutableList()
            }
            map.toImmutableMap()
        }

    private val getGroupedDetectedDiseaseToDamageLevelsToDetectedCocoas =
        { analysisSessionDui: AnalysisSessionDui ->
            val outerMap =
                mutableMapOf<CocoaDisease, ImmutableMap<Int, ImmutableList<DetectedCocoa>>>()
            analysisSessionDui.detectedCocoas.groupBy { it.disease }.map {
                val (cocoaDisease, list) = it.toPair()
                val immutableList = list.toImmutableList()

                val innerMap = mutableMapOf<Int, ImmutableList<DetectedCocoa>>()
                immutableList.groupBy { item ->
                    item.damageLevel.roundToInt()
                }.map { innerItem ->
                    val (damageLevel, innerList) = innerItem.toPair()
                    innerMap[damageLevel] = innerList.toImmutableList()
                }
                outerMap[cocoaDisease] = innerMap.toImmutableMap()
            }
            outerMap.toImmutableMap()
        }

    private fun getDiagnosisResultOverview(analysisSessionDui: AnalysisSessionDui): DiagnosisResultOverviewDui {
        return DiagnosisResultOverviewDui(
            solutionId = analysisSessionDui.solutionId,
            preventionId = analysisSessionDui.preventionsId,
            solutionEn = analysisSessionDui.solutionEn,
            preventionEn = analysisSessionDui.preventionsEn
        )
    }

    private fun getImageBoundingBoxes(analysisSessionDui: AnalysisSessionDui): ImmutableList<BoundingBox> {
        return analysisSessionDui.detectedCocoas.map {
            it.getBoundingBoxWithItsNameAsTheLabel(applicationContext)
        }.toImmutableList()
    }

    private fun getPriceAnalysisOverviewDui(analysisSessionDui: AnalysisSessionDui): PriceAnalysisOverviewDui {
        return PriceAnalysisOverview(
            totalPredictedSellPrice = analysisSessionDui.predictedPrice,
            detectedCocoaCount = analysisSessionDui.detectedCocoas.size,
            cocoaAverageWeight = 0.2f
        ).run { DuiMapper.mapPriceAnalysisOverviewToDui(this) }
    }


    private fun initFromExtras() {
        val backupNewAnalysisSessionIdThatJustSaved = backupNewAnalysisSessionIdThatJustSaved
        val isNewAnalysisSessionSavedFromProcessDeathDueToSystemKills =
            backupNewAnalysisSessionIdThatJustSaved != null
        if (isNewAnalysisSessionSavedFromProcessDeathDueToSystemKills) {
            backupNewAnalysisSessionIdThatJustSaved?.let { sessionId ->
                viewModelScope.launch {
                    getCocoaAnalysisSessionById(sessionId)
                }
            }
            return
        }

        val isFromNewSession =
            extras.newSessionName != null && extras.newUnsavedSessionImagePath != null

        if (isFromNewSession) {
            detectImage(
                extras.newSessionName as String,
                extras.newUnsavedSessionImagePath as String
            )
        } else {
            extras.sessionId?.let { sessionId ->
                viewModelScope.launch {
                    getCocoaAnalysisSessionById(sessionId)
                }
            }

        }
    }

    private suspend fun getCocoaAnalysisSessionById(sessionId: Int) {
        when (val result = getCocoaAnalysisSessionUseCase(sessionId)) {
            is Result.Error -> {
                val errorUIText = result.toErrorUIText()
                _event.send(
                    AnalysisResultUIEvent.OnFailedToFindSession(
                        errorUIText
                    )
                )
            }

            is Result.Success -> {
                val selectedAnalysisSession = result.data
                _presentedAnalysisSessionDui.update {
                    duiMapper.mapAnalysisSessionToDui(
                        selectedAnalysisSession
                    )
                }
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        imagePreviewPath = selectedAnalysisSession.imageUrlOrPath
                    )
                }
            }
        }
    }

    private fun detectImage(
        sessionName: String,
        imagePath: String
    ) {
        if (detectImageJob != null) return

        detectImageJob = viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(isLoading = true, imagePreviewPath = imagePath) }

            when (val result = analysisCocoaUseCase(sessionName, imagePath)) {
                is Result.Error -> {
                    when (result.error) {
                        CocoaAnalysisError.FAILED_TO_DETECT_COCOA -> {
                            _event.send(
                                AnalysisResultUIEvent.OnFailedToAnalyseImage
                            )
                        }

                        CocoaAnalysisError.NO_COCOA_DETECTED -> {
                            _event.send(
                                AnalysisResultUIEvent.OnInputImageInvalid
                            )
                        }

                        else -> {
                            //TODO()
                        }
                    }
                }

                is Result.Success -> {
                    val extras = this@AnalysisResultViewModel.extras
                    if (extras.newSessionName == null || extras.newUnsavedSessionImagePath == null) return@launch

                    val newAnalysisSession = result.data
                    backupNewAnalysisSessionIdThatJustSaved = newAnalysisSession.id

                    coroutineContext.ensureActive()

                    _presentedAnalysisSessionDui.update {
                        duiMapper.mapAnalysisSessionToDui(
                            newAnalysisSession
                        )
                    }
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                        )
                    }

                }
            }
        }.apply {
            invokeOnCompletion {
                _uiState.update { it.copy(isLoading = false) }
                detectImageJob = null
            }
        }
    }

    init {
        initFromExtras()

        viewModelScope.launch {
            _presentedAnalysisSessionDui.collectLatest { diagnosisSessionDui ->
                withContext(Dispatchers.Default) {
                    val groupedDetectedDiseaseToDetectedCocoas =
                        getGroupedDetectedDiseaseToDetectedCocoas(diagnosisSessionDui)
                    val groupedDetectedDiseaseToDamageLevelsToDetectedCocoas =
                        getGroupedDetectedDiseaseToDamageLevelsToDetectedCocoas(diagnosisSessionDui)
                    val priceAnalysisOverviewDui = getPriceAnalysisOverviewDui(diagnosisSessionDui)
                    val imageBoundingBoxes = getImageBoundingBoxes(diagnosisSessionDui)
                    val diagnosisResultOverviewDui = getDiagnosisResultOverview(diagnosisSessionDui)

                    _uiState.update {
                        it.copy(
                            sessionId = diagnosisSessionDui.id,
                            sessionTitle = diagnosisSessionDui.title,
                            groupedDetectedDiseaseToDetectedCocoas = groupedDetectedDiseaseToDetectedCocoas,
                            groupedDetectedDiseaseToDamageLevelsToDetectedCocoas = groupedDetectedDiseaseToDamageLevelsToDetectedCocoas,
                            priceAnalysisOverview = priceAnalysisOverviewDui,
                            imageBoundingBoxes = imageBoundingBoxes,
                            diagnosisResultOverview = diagnosisResultOverviewDui
                        )
                    }

                }
            }
        }

        viewModelScope.launch {
            _multiplier.collectLatest { multiplier ->
                withContext(Dispatchers.Default) {
                    if (unmodifiedAnalysisSessionDui == null || unmodifiedAnalysisSessionDui?.id == invalidSessionId) {
                        unmodifiedAnalysisSessionDui = _presentedAnalysisSessionDui.value.deepCopy()
                    }

                    val original = unmodifiedAnalysisSessionDui

                    if (original != null) {
                        _presentedAnalysisSessionDui.update {
                            original.copy(
                                predictedPrice = (original.predictedPrice * multiplier),
                                detectedCocoas = original.detectedCocoas.map { detectedCocoa ->
                                    detectedCocoa.copy(
                                        predictedPriceInIdr = detectedCocoa.predictedPriceInIdr * multiplier
                                    )
                                }.toImmutableList()
                            )
                        }
                    }

                }
            }
        }
    }

    companion object {
        private const val BACKUP_NEW_ANALYSIS_SESSION_ID_THAT_JUST_SAVED =
            "backupNewAnalysisSessionIdThatJustSaved"
    }

}