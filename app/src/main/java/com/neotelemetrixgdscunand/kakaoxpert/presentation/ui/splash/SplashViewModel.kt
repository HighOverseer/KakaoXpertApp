package com.neotelemetrixgdscunand.kamekapp.presentation.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neotelemetrixgdscunand.kamekapp.domain.data.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _isReadyEvent = Channel<Pair<Boolean, Boolean>>()
    val isReadyEvent = _isReadyEvent.receiveAsFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val isAlreadyLoggedIn = authRepository.isAlreadyLoggedIn()
            val isFirstTime = authRepository.isFirstTime()
            _isReadyEvent.send(Pair(isAlreadyLoggedIn, isFirstTime))
        }
    }

}