package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.diagnosisresult

import com.dicoding.asclepius.domain.common.StringRes

sealed class DiagnosisResultUIEvent {
    data class OnToastMessage(val message: StringRes) : DiagnosisResultUIEvent()
    data object OnInputImageInvalid : DiagnosisResultUIEvent()
}