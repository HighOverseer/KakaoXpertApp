package com.neotelemetrixgdscunand.kamekapp.presentation.ui.auth

import com.neotelemetrixgdscunand.kamekapp.presentation.utils.UIText

sealed interface LoginUIEvent {
    data class OnLoginFailed(val messageUIText: UIText) : LoginUIEvent
    data class OnLoginSuccess(val userName: String, val isFirstTime: Boolean) : LoginUIEvent
}