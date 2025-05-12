package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neotelemetrixgdscunand.kakaoxpert.R
import com.neotelemetrixgdscunand.kakaoxpert.domain.common.PasswordValidator
import com.neotelemetrixgdscunand.kakaoxpert.domain.common.Result
import com.neotelemetrixgdscunand.kakaoxpert.domain.common.UsernameValidator
import com.neotelemetrixgdscunand.kakaoxpert.domain.data.AuthRepository
import com.neotelemetrixgdscunand.kakaoxpert.presentation.utils.UIText
import com.neotelemetrixgdscunand.kakaoxpert.presentation.utils.toErrorUIText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val passwordValidator: PasswordValidator,
    private val usernameValidator: UsernameValidator
) : ViewModel() {

    private var registerJob: Job? = null
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _uiEvent = Channel<RegisterUIEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun register(
        name: String,
        handphoneNumberOrEmail: String,
        password: String,
        passwordConfirmation: String
    ) {
        if (registerJob?.isCompleted == false) return

        registerJob = viewModelScope.launch(Dispatchers.IO) {
            _isLoading.update { true }

            val isNameValid = name.isNotEmpty()
            if (!isNameValid) {
                val errorUIText = UIText.StringResource(R.string.nama_tidak_boleh_kosong)
                _uiEvent.send(
                    RegisterUIEvent.OnRegisterFailed(errorUIText)
                )
                return@launch
            }

            val usernameValidationResult =
                usernameValidator.validateUsername(handphoneNumberOrEmail)
            when (usernameValidationResult) {
                is Result.Error -> {
                    val errorUIText = usernameValidationResult.toErrorUIText()
                    _uiEvent.send(
                        RegisterUIEvent.OnRegisterFailed(errorUIText)
                    )
                    return@launch
                }

                is Result.Success -> {}
            }

            val isPasswordConfirmationValid = password == passwordConfirmation
            if (!isPasswordConfirmationValid) {
                val errorUIText =
                    UIText.StringResource(R.string.password_konfirmasi_tidak_cocok_periksa_lagi)
                _uiEvent.send(
                    RegisterUIEvent.OnRegisterFailed(errorUIText)
                )
                return@launch
            }

            when (val passwordValidationResult = passwordValidator.validatePassword(password)) {
                is Result.Error -> {
                    val errorUIText = passwordValidationResult.toErrorUIText()
                    _uiEvent.send(
                        RegisterUIEvent.OnRegisterFailed(errorUIText)
                    )
                    return@launch
                }

                is Result.Success -> {}
            }

            val result = authRepository.register(
                name = name,
                handphoneNumberOrEmail = handphoneNumberOrEmail,
                password = password,
                passwordConfirmation = passwordConfirmation
            )
            when (result) {
                is Result.Success -> {
                    val userName = result.data.split(" ").firstOrNull() ?: "Anonim"
                    _uiEvent.send(RegisterUIEvent.OnRegisterSuccess(userName))
                }

                is Result.Error -> {
                    val errorUIText = result.toErrorUIText()
                    _uiEvent.send(
                        RegisterUIEvent.OnRegisterFailed(
                            errorUIText
                        )
                    )
                }
            }
        }.also {
            it.invokeOnCompletion {
                _isLoading.update { false }
            }
        }
    }

}