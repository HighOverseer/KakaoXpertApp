package com.neotelemetrixgdscunand.kakaoxpert.domain

import com.neotelemetrixgdscunand.kamekapp.domain.model.DiagnosisSession
import com.neotelemetrixgdscunand.kamekapp.domain.model.DiagnosisSessionPreview

object DomainMapper {
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