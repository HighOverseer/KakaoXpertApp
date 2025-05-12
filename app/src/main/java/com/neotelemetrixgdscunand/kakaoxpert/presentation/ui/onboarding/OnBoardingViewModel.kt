package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neotelemetrixgdscunand.kakaoxpert.domain.data.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _onBoardingSessionFinishedEvent = Channel<Unit>()
    val onBoardingSessionFinishedEvent = _onBoardingSessionFinishedEvent.receiveAsFlow()

    private var job: Job? = null

    fun onBoardingSessionFinish() {
        if (job != null) return

        job = viewModelScope.launch(Dispatchers.IO) {
            authRepository.setIsFirstTime(false)
            _onBoardingSessionFinishedEvent.send(Unit)
        }.also {
            it.invokeOnCompletion {
                job = null
            }
        }
    }

}