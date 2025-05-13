package com.neotelemetrixgdscunand.kakaoxpert.domain.data

import com.neotelemetrixgdscunand.kakaoxpert.domain.model.AnalysisSession
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.AnalysisSessionPreview
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.DetectedCocoa
import kotlinx.coroutines.flow.Flow

interface CocoaAnalysisRepository {
    suspend fun saveDiagnosis(
        sessionName: String,
        imageOrUrlPath: String,
        predictedPrice: Float,
        detectedCocoas: List<DetectedCocoa>
    ): Int

    fun getAllSessionPreviews(): Flow<List<AnalysisSessionPreview>>

    suspend fun getDiagnosisSession(id: Int): AnalysisSession

}