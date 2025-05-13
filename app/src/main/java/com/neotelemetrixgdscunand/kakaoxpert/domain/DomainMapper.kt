package com.neotelemetrixgdscunand.kakaoxpert.domain

import com.neotelemetrixgdscunand.kakaoxpert.domain.model.AnalysisSession
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.AnalysisSessionPreview

object DomainMapper {
    fun mapDiagnosisSessionToPreview(
        analysisSession: AnalysisSession
    ): AnalysisSessionPreview {
        return analysisSession.run {
            AnalysisSessionPreview(
                id, title, imageUrlOrPath, createdAt = createdAt, predictedPrice
            )
        }
    }
}