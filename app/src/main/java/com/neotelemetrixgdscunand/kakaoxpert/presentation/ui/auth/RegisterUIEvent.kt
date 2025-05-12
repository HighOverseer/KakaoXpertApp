package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.auth

import com.neotelemetrixgdscunand.kakaoxpert.presentation.utils.UIText

sealed interface RegisterUIEvent {
    data class OnRegisterFailed(val messageUIText: UIText) : RegisterUIEvent
    data class OnRegisterSuccess(val userName: String) : RegisterUIEvent
}