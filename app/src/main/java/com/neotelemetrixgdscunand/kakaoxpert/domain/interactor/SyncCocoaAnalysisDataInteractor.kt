package com.neotelemetrixgdscunand.kakaoxpert.domain.interactor

import com.neotelemetrixgdscunand.kakaoxpert.domain.common.CocoaAnalysisSyncType
import com.neotelemetrixgdscunand.kakaoxpert.domain.common.DataError
import com.neotelemetrixgdscunand.kakaoxpert.domain.common.Result
import com.neotelemetrixgdscunand.kakaoxpert.domain.common.SyncError
import com.neotelemetrixgdscunand.kakaoxpert.domain.common.SyncSuccess
import com.neotelemetrixgdscunand.kakaoxpert.domain.data.CocoaAnalysisRepository
import com.neotelemetrixgdscunand.kakaoxpert.domain.data.DataPreference
import com.neotelemetrixgdscunand.kakaoxpert.domain.usecase.SyncCocoaAnalysisDataUseCase
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.withContext

class SyncCocoaAnalysisDataInteractor(
    private val cocoaAnalysisRepository: CocoaAnalysisRepository,
    private val dataPreference: DataPreference
):SyncCocoaAnalysisDataUseCase {

    override suspend fun invoke(syncType: CocoaAnalysisSyncType): Result<SyncSuccess, DataError> {
        val needToSync = dataPreference.needToSync(syncType)

        if(!needToSync) return Result.Success(SyncSuccess.ALREADY_SYNCED_BEFORE)

        return try {
            dataPreference.setIsSyncing(syncType, true)
            val result = when(syncType){
                CocoaAnalysisSyncType.PREVIEWS -> cocoaAnalysisRepository.syncAllSessionPreviewsFromRemote()
                CocoaAnalysisSyncType.REMOTE -> cocoaAnalysisRepository.syncAllSavedSessionsFromRemote()
                CocoaAnalysisSyncType.LOCAL -> cocoaAnalysisRepository.syncAllUnsavedSessionsFromLocal()
            }

            when(result){
                is Result.Success -> {
                    dataPreference.updateLastSyncTime(syncType)
                    return Result.Success(SyncSuccess.NORMAL)
                }
                is Result.Error -> Result.Error(result.error)
            }

        }finally {
            withContext(NonCancellable){
                dataPreference.setIsSyncing(syncType, false)
            }
        }
    }
}