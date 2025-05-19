package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.diagnosisresult

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.neotelemetrixgdscunand.kakaoxpert.domain.common.CocoaAnalysisError
import com.neotelemetrixgdscunand.kakaoxpert.domain.common.Result
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.AnalysisSession
import com.neotelemetrixgdscunand.kakaoxpert.domain.usecase.AnalysisCocoaUseCase
import com.neotelemetrixgdscunand.kakaoxpert.domain.usecase.GetCocoaAnalysisSessionUseCase
import com.neotelemetrixgdscunand.kakaoxpert.presentation.mapper.DuiMapper
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.Navigation
import com.neotelemetrixgdscunand.kakaoxpert.presentation.utils.toErrorUIText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class DiagnosisResultViewModel @Inject constructor(
    private val analysisCocoaUseCase: AnalysisCocoaUseCase,
    private val getCocoaAnalysisSessionUseCase: GetCocoaAnalysisSessionUseCase,
    private val savedStateHandle: SavedStateHandle,
    private val duiMapper: DuiMapper
) : ViewModel() {

    // Backup new diagnosis session id that just has been saved, in case process death happens
    private var backupNewDiagnosisSessionIdThatJustSaved: Int?
        get() = savedStateHandle[BACKUP_NEW_DIAGNOSIS_SESSION_ID_THAT_JUST_SAVED]
        set(value) {
            savedStateHandle[BACKUP_NEW_DIAGNOSIS_SESSION_ID_THAT_JUST_SAVED] = value
        }

    private val _uiState = MutableStateFlow(DiagnosisResultUIState())
    val uiState = _uiState.asStateFlow()

    private val _event = Channel<AnalysisResultUIEvent>()
    val event = _event
        .receiveAsFlow()

    private var detectImageJob: Job? = null

    private val extras = savedStateHandle.toRoute<Navigation.DiagnosisResult>()

    init {
        initFromExtras()
    }

    private fun initFromExtras() {
        val backupNewDiagnosisSessionIdThatJustSaved = backupNewDiagnosisSessionIdThatJustSaved
        val isNewDiagnosisSessionSavedFromProcessDeathDueToSystemKills =
            backupNewDiagnosisSessionIdThatJustSaved != null
        if (isNewDiagnosisSessionSavedFromProcessDeathDueToSystemKills) {
            backupNewDiagnosisSessionIdThatJustSaved?.let { sessionId ->
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

    private suspend fun getCocoaAnalysisSessionById(sessionId: Int){
        when(val result = getCocoaAnalysisSessionUseCase(sessionId)){
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
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        diagnosisSession = duiMapper.mapDiagnosisSessionToDui(
                            selectedAnalysisSession
                        ),
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
                    val extras = this@DiagnosisResultViewModel.extras
                    if (extras.newSessionName == null || extras.newUnsavedSessionImagePath == null) return@launch

                    val newAnalysisSession = result.data
                    backupNewDiagnosisSessionIdThatJustSaved = newAnalysisSession.id

                    coroutineContext.ensureActive()

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            diagnosisSession = duiMapper.mapDiagnosisSessionToDui(
                                newAnalysisSession
                            )
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

    companion object {
        private const val BACKUP_NEW_DIAGNOSIS_SESSION_ID_THAT_JUST_SAVED =
            "backupNewDiagnosisSessionIdThatJustSaved"
    }

}