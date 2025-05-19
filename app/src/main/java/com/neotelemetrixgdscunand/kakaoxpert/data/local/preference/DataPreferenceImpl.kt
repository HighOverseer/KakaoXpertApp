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

    private val mapSyncTypeToIsSyncingState = hashMapOf(
        CocoaAnalysisSyncType.PREVIEWS to IS_SYNCING_PREVIEWS_ANALYSIS,
        CocoaAnalysisSyncType.REMOTE to IS_SYNCING_REMOTE_ANALYSIS,
        CocoaAnalysisSyncType.LOCAL to IS_SYNCING_LOCAL_ANALYSIS
    )

    override suspend fun needToSync(syncType: CocoaAnalysisSyncType): Boolean {
        val didPassPeriodThreshold = if(syncType == CocoaAnalysisSyncType.PREVIEWS){
            val lastSyncTime = dataStorePrefs.data.map { it[LAST_SYNC_PREVIEW_ANALYSIS_TIME] ?: 0L }.first()
            System.currentTimeMillis() - lastSyncTime >= DataPreference.SYNC_TIME_PERIOD_IN_MILLIS
        }else true

        val isSyncingKey = mapSyncTypeToIsSyncingState[syncType] ?: return false
        val isSyncing = dataStorePrefs.data.map { it[isSyncingKey] ?: false }.first()

        return !isSyncing && didPassPeriodThreshold
    }

    override suspend fun setIsSyncing(syncType: CocoaAnalysisSyncType, isSyncing: Boolean) {
        val isSyncingKey = mapSyncTypeToIsSyncingState[syncType]
        if (isSyncingKey != null) {
            dataStorePrefs.edit { prefs ->
                prefs[isSyncingKey] = isSyncing
            }
        }
    }

    override suspend fun updateLastSyncTime(syncType: CocoaAnalysisSyncType) {
        if(syncType != CocoaAnalysisSyncType.PREVIEWS) return

        dataStorePrefs.edit { prefs ->
            prefs[LAST_SYNC_PREVIEW_ANALYSIS_TIME] = System.currentTimeMillis()
        }
    }

    override suspend fun resetAll() {
        dataStorePrefs.edit { prefs ->
            prefs[IS_SYNCING_PREVIEWS_ANALYSIS] = false
            prefs[IS_SYNCING_REMOTE_ANALYSIS] = false
            prefs[IS_SYNCING_LOCAL_ANALYSIS] = false

            prefs[LAST_SYNC_PREVIEW_ANALYSIS_TIME] = 0L
        }
    }

    companion object {

        private val IS_SYNCING_PREVIEWS_ANALYSIS =
            booleanPreferencesKey("is_syncing_previews_analysis")
        private val IS_SYNCING_REMOTE_ANALYSIS = booleanPreferencesKey("is_syncing_remote_analysis")
        private val IS_SYNCING_LOCAL_ANALYSIS = booleanPreferencesKey("is_syncing_local_analysis")

        private val LAST_SYNC_PREVIEW_ANALYSIS_TIME =
            longPreferencesKey("last_sync_preview_analysis_time")
    }
}