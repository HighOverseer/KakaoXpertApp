package com.neotelemetrixgdscunand.kakaoxpert.domain.model

data class PriceAnalysisOverview(
    val detectedCocoaCount: Int,
    val cocoaAverageWeight: Float,
    val totalPredictedSellPrice: Float
)