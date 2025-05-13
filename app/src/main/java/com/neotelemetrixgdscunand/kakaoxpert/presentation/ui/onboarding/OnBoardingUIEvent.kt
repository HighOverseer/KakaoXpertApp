package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.onboarding

import com.neotelemetrixgdscunand.kakaoxpert.presentation.utils.UIText

sealed interface OnBoardingUIEvent {
    data object OnBoardingSessionFinished : OnBoardingUIEvent
    data class OnFailedFinishingSession(val errorUIText: UIText) : OnBoardingUIEvent
}