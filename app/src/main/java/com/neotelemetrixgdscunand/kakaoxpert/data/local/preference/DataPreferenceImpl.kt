package com.neotelemetrixgdscunand.kakaoxpert.data.local.preference

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import com.neotelemetrixgdscunand.kakaoxpert.domain.data.DataPreference
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataPreferenceImpl @Inject constructor(
    private val dataStorePrefs: DataStore<Preferences>
):DataPreference {

    override suspend fun needToSync(): Boolean {
        val lastSyncTime = dataStorePrefs.data.map { prefs ->
            prefs[LAST_SYNC_TIME] ?: 0L
        }.first()

        val isSyncing = dataStorePrefs.data.map { prefs ->
            prefs[IS_SYNCING] ?: false
        }.first()

        val passSyncPeriodThreshold = System.currentTimeMillis() - lastSyncTime >= DataPreference.SYNC_TIME_PERIOD

        val needToSync = !isSyncing && passSyncPeriodThreshold

        return needToSync
    }


    override suspend fun setIsSyncing(isSyncing: Boolean) {
        dataStorePrefs.edit { prefs ->
            prefs[IS_SYNCING] = isSyncing
        }
    }

    override suspend fun updateLastSyncTime() {
        dataStorePrefs.edit { prefs ->
            prefs[LAST_SYNC_TIME] = System.currentTimeMillis()
        }
    }

    companion object{

        private val IS_SYNCING = booleanPreferencesKey("is_syncing")
        private val LAST_SYNC_TIME = longPreferencesKey("last_sync_time")
    }
}