package com.neotelemetrixgdscunand.kakaoxpert.presentation.utils

import com.google.i18n.phonenumbers.NumberParseException
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.neotelemetrixgdscunand.kakaoxpert.domain.common.Result
import com.neotelemetrixgdscunand.kakaoxpert.domain.common.UsernameValidator
import javax.inject.Inject

class UsernameValidatorImpl @Inject constructor(
    private val phoneNumberUtil: PhoneNumberUtil
) : UsernameValidator {
    override fun validateUsername(username: String): Result<Unit, UsernameValidator.UsernameError> {
        if (username.isBlank()) {
            return Result.Error(UsernameValidator.UsernameError.EMPTY)
        }

        val isUsernameTooShort = username.length < 6
        if (isUsernameTooShort) {
            return Result.Error(UsernameValidator.UsernameError.TOO_SHORT)
        }

        val isUsernameEmail = username.contains("@")
        val isUsernamePhoneNumber = username.contains("+")

        val isInValidFormat = isUsernameEmail || isUsernamePhoneNumber
        if (!isInValidFormat) {
            return Result.Error(UsernameValidator.UsernameError.NOT_IN_VALID_FORMAT)
        }

        when {
            isUsernamePhoneNumber -> {
                val defaultRegion = "ID"
                return try {
                    val numberProto = phoneNumberUtil.parse(username, defaultRegion)
                    val isValid = phoneNumberUtil.isValidNumber(numberProto)

                    if (isValid) Result.Success(Unit) else Result.Error(UsernameValidator.UsernameError.NOT_IN_VALID_FORMAT)

                } catch (e: NumberParseException) {
                    return Result.Error(UsernameValidator.UsernameError.NOT_IN_VALID_FORMAT)
                }
            }

            else -> return Result.Success(Unit)
        }
    }
}