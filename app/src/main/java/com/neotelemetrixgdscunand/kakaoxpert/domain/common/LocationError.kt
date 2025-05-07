package com.neotelemetrixgdscunand.kamekapp.domain.common

sealed interface LocationError : Error {
    data class ResolvableSettingsError(val exception: Exception) : LocationError
    data object UnknownError : LocationError
    data class UnexpectedErrorWithMessage(val message: String) : LocationError
}