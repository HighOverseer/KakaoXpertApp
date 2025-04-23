package com.neotelemetrixgdscunand.kakaoxpert.domain

import com.neotelemetrixgdscunand.kakaoxpert.domain.model.DiagnosisSession
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.DiagnosisSessionPreview

object Mapper {
    fun mapDiagnosisSessionToPreview(
        diagnosisSession: DiagnosisSession
    ): DiagnosisSessionPreview {
        return diagnosisSession.run {
            DiagnosisSessionPreview(
                id, title, imageUrlOrPath, date, predictedPrice
            )
        }
    }
}