package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.analysissessionresult

import com.neotelemetrixgdscunand.kakaoxpert.presentation.utils.UIText

sealed class AnalysisResultUIEvent {
    data object OnFailedToAnalyseImage : AnalysisResultUIEvent()
    data object OnInputImageInvalid : AnalysisResultUIEvent()
    data class OnFailedToFindSession(val errorUIText: UIText) : AnalysisResultUIEvent()
    data class OnInputCocoaAverageWeightInvalid(val errorUIText: UIText) : AnalysisResultUIEvent()
}