package com.neotelemetrixgdscunand.kakaoxpert.domain

import com.neotelemetrixgdscunand.kakaoxpert.domain.model.AnalysisSession
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.DiagnosisSessionPreview

object DomainMapper {
    fun mapDiagnosisSessionToPreview(
        analysisSession: AnalysisSession
    ): DiagnosisSessionPreview {
        return analysisSession.run {
            DiagnosisSessionPreview(
                id, title, imageUrlOrPath, date, predictedPrice
            )
        }
    }
}