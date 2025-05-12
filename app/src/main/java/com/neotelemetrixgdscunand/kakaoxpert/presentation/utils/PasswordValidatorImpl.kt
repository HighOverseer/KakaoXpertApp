package com.neotelemetrixgdscunand.kakaoxpert.presentation.utils

import com.neotelemetrixgdscunand.kakaoxpert.domain.common.PasswordValidator
import com.neotelemetrixgdscunand.kakaoxpert.domain.common.Result
import javax.inject.Inject

class PasswordValidatorImpl @Inject constructor() : PasswordValidator {
    override fun validatePassword(password: String): Result<Unit, PasswordValidator.PasswordError> {
        if (password.isBlank()) {
            return Result.Error(PasswordValidator.PasswordError.EMPTY)
        }

        if (password.length < 8) {
            return Result.Error(PasswordValidator.PasswordError.TOO_SHORT)
        }

        val isContainUppercase = password.any { it.isUpperCase() }
        if (!isContainUppercase) {
            return Result.Error(PasswordValidator.PasswordError.NO_UPPERCASE)
        }

        val isContainDigit = password.any { it.isDigit() }
        if (!isContainDigit) {
            return Result.Error(PasswordValidator.PasswordError.NO_DIGIT)
        }

        return Result.Success(Unit)
    }
}