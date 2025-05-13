package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.diagnosisresult

import com.neotelemetrixgdscunand.kakaoxpert.presentation.dui.AnalysisSessionDui

data class DiagnosisResultUIState(
    val isLoading: Boolean = false,
    val diagnosisSession: AnalysisSessionDui = AnalysisSessionDui(),
    val imagePreviewPath: String? = null
)