package com.neotelemetrixgdscunand.kakaoxpert.domain.common

enum class AuthError : DataError.NetworkError {
    INVALID_TOKEN,
    INVALID_REGISTER_SESSION,
    USERNAME_IS_ALREADY_REGISTERED,
    INCORRECT_USERNAME_OR_PASSWORD,
}