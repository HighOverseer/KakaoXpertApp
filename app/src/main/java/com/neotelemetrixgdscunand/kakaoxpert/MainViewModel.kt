package com.neotelemetrixgdscunand.kakaoxpert

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neotelemetrixgdscunand.kakaoxpert.domain.common.CocoaAnalysisSyncType
import com.neotelemetrixgdscunand.kakaoxpert.domain.common.Result
import com.neotelemetrixgdscunand.kakaoxpert.domain.common.SyncSuccess
import com.neotelemetrixgdscunand.kakaoxpert.domain.usecase.SyncCocoaAnalysisDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val syncCocoaAnalysisDataUseCase: SyncCocoaAnalysisDataUseCase
) : ViewModel() {

    init {
        viewModelScope.launch {
            syncAnyCocoaAnalysisTypeThatNeedTo()
        }
    }

    private suspend fun syncAnyCocoaAnalysisTypeThatNeedTo() {
        for (syncType in CocoaAnalysisSyncType.entries) {
            val result = syncCocoaAnalysisDataUseCase(syncType)

            if (result is Result.Success && result.data == SyncSuccess.ALREADY_SYNCED_OR_IN_SYNCING) {
                continue
            }

            break
        }

    }
}