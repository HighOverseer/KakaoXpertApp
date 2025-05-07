package com.neotelemetrixgdscunand.kamekapp.data

import com.neotelemetrixgdscunand.kakaoxpert.data.remote.ApiService
import com.neotelemetrixgdscunand.kamekapp.data.utils.fetchFromNetwork
import com.neotelemetrixgdscunand.kamekapp.domain.common.AuthError
import com.neotelemetrixgdscunand.kamekapp.domain.common.DataError
import com.neotelemetrixgdscunand.kamekapp.domain.common.Result
import com.neotelemetrixgdscunand.kamekapp.domain.data.AuthPreference
import com.neotelemetrixgdscunand.kamekapp.domain.data.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val authPreference: AuthPreference,
) : AuthRepository {

    override suspend fun login(
        handphoneNumberOrEmail: String,
        password: String
    ): Result<Pair<String, Boolean>, DataError.NetworkError> {
        return fetchFromNetwork(
            fetching = {
                val response = apiService.login(
                    handphoneNumberOrEmail, password
                )

                val token = response.data?.token
                val userId = response.data?.userId
                val userName = response.data?.userName

                val isTokenValid = token != null && userId != null && userName != null

                if (!isTokenValid) {
                    return@fetchFromNetwork Result.Error(AuthError.INVALID_TOKEN)
                }

                authPreference.saveToken(token as String)
                val isFirstTime = authPreference.getIsFirstTime().first()

                val data = Pair(userName as String, isFirstTime)
                return@fetchFromNetwork Result.Success(data)
            },
            getErrorFromStatusCode = { statusCode ->
                return@fetchFromNetwork when (statusCode) {
                    404 -> AuthError.INCORRECT_USERNAME_OR_PASSWORD
                    else -> null
                }
            }
        )
    }

    override suspend fun register(
        handphoneNumberOrEmail: String,
        password: String,
        passwordConfirmation: String,
        name: String
    ): Result<String, DataError.NetworkError> = withContext(Dispatchers.IO) {
        val oldSavedValueIsFirstTime = authPreference.getIsFirstTime().first()

        fetchFromNetwork(
            fetching = {
                val response = apiService.register(
                    handphoneNumberOrEmail, password, passwordConfirmation, name
                )

                val userName = response.data?.userName
                val token = response.data?.token

                val isRegisterValid =
                    response.data?.userId != null && token != null && userName != null

                if (!isRegisterValid) {
                    return@fetchFromNetwork Result.Error(AuthError.INVALID_REGISTER_SESSION)
                }

                listOf(
                    launch { authPreference.saveToken(token as String) },
                    launch { authPreference.saveIsFirstTime(true) }
                ).joinAll()

                return@fetchFromNetwork Result.Success(userName as String)
            },
            getErrorFromStatusCode = { statusCode ->
                return@fetchFromNetwork when (statusCode) {
                    400 -> AuthError.USERNAME_IS_ALREADY_REGISTERED
                    else -> null
                }
            },
            nonCancellableBlockWhenException = {
                authPreference.clearToken()
                authPreference.saveIsFirstTime(oldSavedValueIsFirstTime)
            }
        )
    }

    override suspend fun clearToken() {
        authPreference.clearToken()
    }

    override suspend fun setIsFirstTime(isFirstTime: Boolean) {
        authPreference.saveIsFirstTime(isFirstTime)
    }

    override suspend fun isAlreadyLoggedIn(): Boolean {
        val token = authPreference.getToken().first()
        val isAlreadyLoggedIn = token != ""
        return isAlreadyLoggedIn
    }

    override suspend fun isFirstTime(): Boolean {
        return authPreference.getIsFirstTime().first()
    }
}