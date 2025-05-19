package com.neotelemetrixgdscunand.kakaoxpert.domain.common

typealias RootNetworkError = DataError.NetworkError.ApiError

sealed interface DataError : Error {
    sealed interface NetworkError : DataError {
        enum class ApiError : NetworkError {
            CONNECTIVITY_UNAVAILABLE,
            REQUEST_TIMEOUT,
            NO_CONNECTIVITY_OR_SERVER_UNREACHABLE,
            BAD_REQUEST,
            UNAUTHORIZED,
            FORBIDDEN,
            NOT_FOUND,
            RESOURCE_CONFLICT,
            SERVER_UNAVAILABLE,
            INTERNAL_SERVER_ERROR,
            UNEXPECTED_ERROR
        }
    }
}