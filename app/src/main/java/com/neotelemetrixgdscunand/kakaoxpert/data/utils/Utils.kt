package com.neotelemetrixgdscunand.kakaoxpert.data.utils

import com.neotelemetrixgdscunand.kakaoxpert.domain.common.DataError
import com.neotelemetrixgdscunand.kakaoxpert.domain.common.Result
import com.neotelemetrixgdscunand.kakaoxpert.domain.common.RootNetworkError
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import kotlin.coroutines.cancellation.CancellationException

val mapStatusCodeToError = hashMapOf(
    400 to RootNetworkError.BAD_REQUEST,
    401 to RootNetworkError.UNAUTHORIZED,
    403 to RootNetworkError.FORBIDDEN,
    500 to RootNetworkError.INTERNAL_SERVER_ERROR,
    503 to RootNetworkError.SERVER_UNAVAILABLE
)

val mapIOExceptionToError = hashMapOf(
    UnknownHostException::class to RootNetworkError.CONNECTIVITY_UNAVAILABLE,
    ConnectException::class to RootNetworkError.NO_CONNECTIVITY_OR_SERVER_UNREACHABLE,
    SocketTimeoutException::class to RootNetworkError.REQUEST_TIMEOUT
)

suspend fun <D> fetchFromNetwork(
    fetching: suspend () -> Result<D, DataError.NetworkError>,
    getErrorFromStatusCode: (Int) -> DataError.NetworkError? = { null },
    getErrorFromIOException: (Exception) -> DataError.NetworkError? = { null },
    nonCancellableBlockWhenException: suspend () -> Unit = { }
): Result<D, DataError.NetworkError> {
    return try {
        fetching()
    } catch (e: HttpException) {
        val statusCode = e.code()
        val error = getErrorFromStatusCode(statusCode) ?: mapStatusCodeToError[statusCode]
        ?: RootNetworkError.UNEXPECTED_ERROR
        return Result.Error(error)

    } catch (e: Exception) {
        withContext(NonCancellable) {
            nonCancellableBlockWhenException()
        }

        if (e is CancellationException) throw e

        val error = getErrorFromIOException(e) ?: mapIOExceptionToError[e::class]
        ?: RootNetworkError.UNEXPECTED_ERROR
        return Result.Error(error)
    }
}

suspend fun <D> fetchFromNetwork(
    fetching: suspend () -> Result<D, DataError.NetworkError>,
): Result<D, DataError.NetworkError> {
    return try {
        fetching()
    } catch (e: HttpException) {
        val statusCode = e.code()
        val error =
            mapStatusCodeToError[statusCode] ?: DataError.NetworkError.ApiError.UNEXPECTED_ERROR
        return Result.Error(error)

    } catch (e: Exception) {
        if (e is CancellationException) throw e

        val error =
            mapIOExceptionToError[e::class] ?: DataError.NetworkError.ApiError.UNEXPECTED_ERROR
        return Result.Error(error)
    }
}