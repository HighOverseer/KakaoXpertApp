package com.neotelemetrixgdscunand.kakaoxpert.domain.interactor

import com.neotelemetrixgdscunand.kakaoxpert.domain.data.CocoaAnalysisRepository
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.AnalysisSession
import com.neotelemetrixgdscunand.kakaoxpert.domain.usecase.GetCocoaAnalysisSessionUseCase

class GetCocoaAnalysisSessionInteractor(
    private val cocoaAnalysisRepository: CocoaAnalysisRepository
) : GetCocoaAnalysisSessionUseCase {
    override suspend fun invoke(id: Int): AnalysisSession {
        return cocoaAnalysisRepository.getDiagnosisSession(id)
    }
}