package com.neotelemetrixgdscunand.kakaoxpert.domain.usecase

import com.neotelemetrixgdscunand.kakaoxpert.domain.common.DataError
import com.neotelemetrixgdscunand.kakaoxpert.domain.common.Result
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.AnalysisSession

interface AnalysisCocoaUseCase {
    suspend operator fun invoke(
        sessionName: String,
        imagePath: String
    ): Result<AnalysisSession, DataError>
}