package com.neotelemetrixgdscunand.kakaoxpert.domain.common

import com.neotelemetrixgdscunand.kakaoxpert.domain.common.DataError

enum class AuthError : DataError.NetworkError {
    INVALID_TOKEN,
    INVALID_REGISTER_SESSION,
    USERNAME_IS_ALREADY_REGISTERED,
    INCORRECT_USERNAME_OR_PASSWORD,
}