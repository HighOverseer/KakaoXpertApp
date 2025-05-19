package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.diagnosisresult

import com.neotelemetrixgdscunand.kakaoxpert.presentation.utils.UIText

sealed class AnalysisResultUIEvent {
    data object OnFailedToAnalyseImage : AnalysisResultUIEvent()
    data object OnInputImageInvalid : AnalysisResultUIEvent()
    data class OnFailedToFindSession(val errorUIText: UIText): AnalysisResultUIEvent()
}