package com.neotelemetrixgdscunand.kakaoxpert.domain.common

interface UsernameValidator {

    fun validateUsername(username: String): Result<Unit, UsernameError>

    enum class UsernameError : Error {
        EMPTY,
        NOT_IN_VALID_FORMAT,
        TOO_SHORT,
    }
}