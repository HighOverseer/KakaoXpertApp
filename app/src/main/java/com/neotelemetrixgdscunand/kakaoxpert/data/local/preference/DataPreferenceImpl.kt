package com.neotelemetrixgdscunand.kakaoxpert.data.local.preference

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import com.neotelemetrixgdscunand.kakaoxpert.domain.common.CocoaAnalysisSyncType
import com.neotelemetrixgdscunand.kakaoxpert.domain.data.DataPreference
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataPreferenceImpl @Inject constructor(
    private val dataStorePrefs: DataStore<Preferences>
) : DataPreference {

    private val mapSyncTypeToLastSyncTimeKey = hashMapOf(
        CocoaAnalysisSyncType.PREVIEWS to LAST_SYNC_PREVIEW_ANALYSIS_TIME,
        CocoaAnalysisSyncType.REMOTE to LAST_SYNC_REMOTE_ANALYSIS_TIME,
        CocoaAnalysisSyncType.LOCAL to LAST_SYNC_LOCAL_ANALYSIS_TIME
    )

    private val mapSyncTypeToIsSyncingState = hashMapOf(
        CocoaAnalysisSyncType.PREVIEWS to IS_SYNCING_PREVIEWS_ANALYSIS,
        CocoaAnalysisSyncType.REMOTE to IS_SYNCING_REMOTE_ANALYSIS,
        CocoaAnalysisSyncType.LOCAL to IS_SYNCING_LOCAL_ANALYSIS
    )

    override suspend fun needToSync(syncType: CocoaAnalysisSyncType): Boolean {
        val lastSyncTimeKey = mapSyncTypeToLastSyncTimeKey[syncType] ?: return false
        val lastSyncTime = dataStorePrefs.data.map { it[lastSyncTimeKey] ?: 0L }.first()
        val didPassPeriodThreshold = System.currentTimeMillis() - lastSyncTime >= DataPreference.SYNC_TIME_PERIOD_IN_MILLIS

        val isSyncingKey = mapSyncTypeToIsSyncingState[syncType] ?: return false
        val isSyncing = dataStorePrefs.data.map { it[isSyncingKey] ?: false }.first()

        return !isSyncing && didPassPeriodThreshold
    }

    override suspend fun setIsSyncing(syncType: CocoaAnalysisSyncType, isSyncing: Boolean) {
        val isSyncingKey = mapSyncTypeToIsSyncingState[syncType]
        if(isSyncingKey != null){
            dataStorePrefs.edit { prefs ->
                prefs[isSyncingKey] = isSyncing
            }
        }
    }

    override suspend fun updateLastSyncTime(syncType: CocoaAnalysisSyncType) {
        val lastTimeSyncingKey = mapSyncTypeToLastSyncTimeKey[syncType]
        if(lastTimeSyncingKey != null){
            dataStorePrefs.edit { prefs ->
                prefs[lastTimeSyncingKey] = System.currentTimeMillis()
            }
        }
    }


    companion object {

        private val IS_SYNCING_PREVIEWS_ANALYSIS = booleanPreferencesKey("is_syncing_previews_analysis")
        private val IS_SYNCING_REMOTE_ANALYSIS = booleanPreferencesKey("is_syncing_remote_analysis")
        private val IS_SYNCING_LOCAL_ANALYSIS = booleanPreferencesKey("is_syncing_local_analysis")

        private val LAST_SYNC_PREVIEW_ANALYSIS_TIME = longPreferencesKey("last_sync_preview_analysis_time")
        private val LAST_SYNC_REMOTE_ANALYSIS_TIME = longPreferencesKey("last_sync_remote_analysis_time")
        private val LAST_SYNC_LOCAL_ANALYSIS_TIME = longPreferencesKey("last_sync_local_analysis_time")
    }
}