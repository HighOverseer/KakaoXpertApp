package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neotelemetrixgdscunand.kakaoxpert.domain.common.CocoaAnalysisSyncType
import com.neotelemetrixgdscunand.kakaoxpert.domain.common.Result
import com.neotelemetrixgdscunand.kakaoxpert.domain.data.AuthRepository
import com.neotelemetrixgdscunand.kakaoxpert.domain.usecase.SyncCocoaAnalysisDataUseCase
import com.neotelemetrixgdscunand.kakaoxpert.presentation.utils.toErrorUIText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val syncCocoaAnalysisDataUseCase: SyncCocoaAnalysisDataUseCase
) : ViewModel() {

    private val _uiEvent = Channel<OnBoardingUIEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _userHasPressedStartButton = MutableStateFlow(false)
    private val _isSyncSuccess = MutableStateFlow(false)

    val isButtonEnabled = _userHasPressedStartButton.map { hasPressed ->
        !hasPressed
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000L),
        false
    )

    private var syncJob: Job? = null

    init {
        syncAllSessions()

        viewModelScope.launch {
            combine(
                _isSyncSuccess,
                _userHasPressedStartButton
            ) { isSyncSuccess, userHasPressedStartButton ->
                isSyncSuccess && userHasPressedStartButton
            }.collectLatest { isSessionFinished ->
                if (isSessionFinished) _uiEvent.send(OnBoardingUIEvent.OnBoardingSessionFinished)
            }
        }
    }

    private fun syncAllSessions() {
        syncJob = viewModelScope.launch(Dispatchers.IO) {
            when (val result =
                syncCocoaAnalysisDataUseCase(CocoaAnalysisSyncType.SELL_PRICE_INFO)) {
                is Result.Error -> {
                    val errorUIText = result.toErrorUIText()
                    _uiEvent.send(OnBoardingUIEvent.OnFailedFinishingSession(errorUIText))
                    _userHasPressedStartButton.update { false }
                    return@launch
                }

                is Result.Success -> { /*Proceed to the next sync*/
                }
            }


            when (val result = syncCocoaAnalysisDataUseCase(CocoaAnalysisSyncType.PREVIEWS)) {
                is Result.Error -> {
                    val errorUIText = result.toErrorUIText()
                    _uiEvent.send(OnBoardingUIEvent.OnFailedFinishingSession(errorUIText))
                    _userHasPressedStartButton.update { false }
                }

                is Result.Success -> {
                    authRepository.setIsFirstTime(false)
                    _isSyncSuccess.update { true }
                }
            }
        }
    }

    fun onUserPressedStartButton() {
        if (syncJob?.isCompleted == true && !_isSyncSuccess.value) {
            syncAllSessions()
        }

        _userHasPressedStartButton.update { true }
    }

}