package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.auth

import com.neotelemetrixgdscunand.kakaoxpert.presentation.utils.UIText

sealed interface LoginUIEvent {
    data class OnLoginFailed(val messageUIText: UIText) : LoginUIEvent
    data class OnLoginSuccess(val userName: String, val isFirstTime: Boolean) : LoginUIEvent
}