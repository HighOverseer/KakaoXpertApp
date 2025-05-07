package com.neotelemetrixgdscunand.kamekapp.domain.data

import kotlinx.coroutines.flow.Flow

interface AuthPreference {

    suspend fun saveToken(token: String)

    fun getToken(): Flow<String>

    suspend fun saveIsFirstTime(isFirstTime: Boolean)

    fun getIsFirstTime(): Flow<Boolean>

    suspend fun clearToken()

    companion object {
        const val NAME = "auth_preference"
    }
}