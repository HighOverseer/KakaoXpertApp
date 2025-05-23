package com.neotelemetrixgdscunand.kakaoxpert.domain.data

import com.neotelemetrixgdscunand.kakaoxpert.domain.common.CocoaAnalysisSyncType
import kotlinx.coroutines.flow.Flow

interface DataPreference {

    suspend fun needToSync(syncType: CocoaAnalysisSyncType): Boolean

    suspend fun setIsSyncing(syncType: CocoaAnalysisSyncType, isSyncing: Boolean)

    suspend fun updateLastSyncTime(syncType: CocoaAnalysisSyncType)

    suspend fun resetAll()

    companion object {
        const val PRICE_INFO_SYNC_TIME_PERIOD_IN_MILLIS = 6 * 60 * 60 * 1000L // 6 hours

        const val SYNC_TIME_PERIOD_IN_MILLIS = 15 * 60 * 1000L // 15 Minutes
        const val SYNC_BACK_OFF_DELAY = 15 * 1000L // 15 Seconds
        const val FALLBACK_ONE_TIME_REQUEST_DELAY = 20 * 60 * 1000L // 20 Minutes
    }
}