package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.diagnosisresult

sealed class AnalysisResultUIEvent {
    data object OnFailedToAnalyseImage : AnalysisResultUIEvent()
    data object OnInputImageInvalid : AnalysisResultUIEvent()
}