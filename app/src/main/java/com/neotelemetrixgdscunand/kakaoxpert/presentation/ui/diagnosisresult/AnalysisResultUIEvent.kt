package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.diagnosisresult

import com.neotelemetrixgdscunand.kakaoxpert.domain.common.StringRes

sealed class AnalysisResultUIEvent {
    data object OnFailedToAnalyseImage : AnalysisResultUIEvent()
    data object OnInputImageInvalid : AnalysisResultUIEvent()
}