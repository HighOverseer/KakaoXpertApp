package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.splash

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neotelemetrixgdscunand.kakaoxpert.domain.data.AuthRepository
import com.neotelemetrixgdscunand.kakaoxpert.presentation.worker.CocoaAnalysisSyncScheduler
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    @ApplicationContext context: Context
) : ViewModel() {

    private val _isReadyEvent = Channel<Pair<Boolean, Boolean>>()
    val isReadyEvent = _isReadyEvent.receiveAsFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val isAlreadyLoggedIn = authRepository.isAlreadyLoggedIn()
            val isFirstTime = authRepository.isFirstTime()
            if (isAlreadyLoggedIn && !isFirstTime) {
                CocoaAnalysisSyncScheduler.startPeriodicSync(context)
            }

            _isReadyEvent.send(Pair(isAlreadyLoggedIn, isFirstTime))
        }
    }

}