package com.neotelemetrixgdscunand.kakaoxpert.domain.presentation


sealed interface CocoaPredictionResult {
    data class Success(
        val prices: List<Float>
    ) : CocoaPredictionResult

    data class Error(
        val exception: Exception
    ) : CocoaPredictionResult
}