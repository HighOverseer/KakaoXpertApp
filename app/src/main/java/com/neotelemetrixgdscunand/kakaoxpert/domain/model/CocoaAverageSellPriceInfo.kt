package com.neotelemetrixgdscunand.kakaoxpert.domain.model

data class CocoaAverageSellPriceInfo(
    val currentAveragePrice: Float,
    val previousAveragePrice: Float?,
    val rateFromPrevious: Float?,
    val time: Long,
)