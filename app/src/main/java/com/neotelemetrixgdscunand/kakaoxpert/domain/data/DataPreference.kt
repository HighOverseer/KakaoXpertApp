package com.neotelemetrixgdscunand.kakaoxpert.domain.data

interface DataPreference {

    suspend fun needToSync(): Boolean

    suspend fun setIsSyncing(isSyncing: Boolean)

    suspend fun updateLastSyncTime()

    companion object {
        const val SYNC_TIME_PERIOD = 15 * 60 * 1000 // 15 Minutes
    }
}