package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.diagnosisresult

import com.neotelemetrixgdscunand.kakaoxpert.presentation.dui.DiagnosisSessionDui

data class DiagnosisResultUIState(
    val isLoading: Boolean = false,
    val diagnosisSession: DiagnosisSessionDui = DiagnosisSessionDui(),
    val imagePreviewPath: String? = null
)