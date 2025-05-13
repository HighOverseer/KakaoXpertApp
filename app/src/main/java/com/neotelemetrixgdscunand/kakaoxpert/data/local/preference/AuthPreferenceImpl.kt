package com.neotelemetrixgdscunand.kakaoxpert.data.local.preference

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.neotelemetrixgdscunand.kakaoxpert.domain.data.AuthPreference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthPreferenceImpl @Inject constructor(
    private val dataStorePrefs: DataStore<Preferences>
) : AuthPreference {

    override suspend fun saveToken(token: String) {
        dataStorePrefs.edit { prefs ->
            prefs[TOKEN] = token
        }
    }

    override fun getToken(): Flow<String> {
        return dataStorePrefs.data.map {
            it[TOKEN] ?: ""
        }
    }

    override suspend fun saveIsFirstTime(isFirstTime: Boolean) {
        dataStorePrefs.edit { prefs ->
            prefs[IS_FIRST_TIME] = isFirstTime
        }
    }

    override fun getIsFirstTime(): Flow<Boolean> {
        return dataStorePrefs.data.map {
            it[IS_FIRST_TIME] ?: true
        }
    }

    override suspend fun clearToken() {
        dataStorePrefs.edit {
            it[TOKEN] = ""
        }
    }

    companion object {
        private val TOKEN = stringPreferencesKey("token")
        private val IS_FIRST_TIME = booleanPreferencesKey("is_first_time")
    }

}