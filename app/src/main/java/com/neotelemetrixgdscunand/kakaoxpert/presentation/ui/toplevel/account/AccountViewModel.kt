package com.neotelemetrixgdscunand.kamekapp.presentation.ui.toplevel.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neotelemetrixgdscunand.kamekapp.domain.data.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _onLogoutFinishedEvent = Channel<Unit>()
    val onLogoutFinishedEvent = _onLogoutFinishedEvent.receiveAsFlow()

    private var job: Job? = null

    fun logout() {
        if (job?.isCompleted == false) return

        job = viewModelScope.launch {
            authRepository.clearToken()
            _onLogoutFinishedEvent.send(Unit)
        }
    }
}