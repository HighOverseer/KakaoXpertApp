package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.toplevel.account

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neotelemetrixgdscunand.kakaoxpert.domain.usecase.LogoutUseCase
import com.neotelemetrixgdscunand.kakaoxpert.presentation.worker.CocoaAnalysisSyncScheduler
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val logoutUseCase: LogoutUseCase,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _onLogoutFinishedEvent = Channel<Unit>()
    val onLogoutFinishedEvent = _onLogoutFinishedEvent.receiveAsFlow()

    private var job: Job? = null

    fun logout() {
        if (job?.isCompleted == false) return

        job = viewModelScope.launch(Dispatchers.IO) {
            logoutUseCase()
            CocoaAnalysisSyncScheduler.stopPeriodicSync(context.applicationContext)
            _onLogoutFinishedEvent.send(Unit)
        }
    }
}