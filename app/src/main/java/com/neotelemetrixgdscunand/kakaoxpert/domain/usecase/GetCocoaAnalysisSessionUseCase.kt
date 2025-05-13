package com.neotelemetrixgdscunand.kakaoxpert.domain.usecase

import com.neotelemetrixgdscunand.kakaoxpert.domain.common.DataError
import com.neotelemetrixgdscunand.kakaoxpert.domain.common.Result
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.AnalysisSession

interface GetCocoaAnalysisSessionUseCase {
    suspend operator fun invoke(id: Int): AnalysisSession
}