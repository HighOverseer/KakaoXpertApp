package com.neotelemetrixgdscunand.kamekapp.domain.common

interface PasswordValidator {

    fun validatePassword(password: String): Result<Unit, PasswordError>

    enum class PasswordError : Error {
        EMPTY,
        TOO_SHORT,
        NO_UPPERCASE,
        NO_DIGIT,
    }
}