package com.neotelemetrixgdscunand.kamekapp.domain.data

import com.neotelemetrixgdscunand.kamekapp.domain.common.DataError
import com.neotelemetrixgdscunand.kamekapp.domain.common.Result

interface AuthRepository {
    suspend fun login(
        handphoneNumberOrEmail: String,
        password: String
    ): Result<Pair<String, Boolean>, DataError.NetworkError>

    suspend fun register(
        handphoneNumberOrEmail: String,
        password: String,
        passwordConfirmation: String,
        name: String,
    ): Result<String, DataError.NetworkError>

    suspend fun clearToken()

    suspend fun setIsFirstTime(isFirstTime: Boolean)

    suspend fun isAlreadyLoggedIn(): Boolean

    suspend fun isFirstTime(): Boolean
}