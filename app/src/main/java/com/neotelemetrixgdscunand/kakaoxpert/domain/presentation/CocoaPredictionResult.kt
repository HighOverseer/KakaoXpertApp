package com.neotelemetrixgdscunand.kakaoxpert.domain.presentation

import com.neotelemetrixgdscunand.kakaoxpert.domain.model.BoundingBox


sealed interface CocoaPredictionResult {
    data class Success(
        val prices: List<Float>
    ) : CocoaPredictionResult

    data class Error(
        val exception: Exception
    ) : CocoaPredictionResult
}