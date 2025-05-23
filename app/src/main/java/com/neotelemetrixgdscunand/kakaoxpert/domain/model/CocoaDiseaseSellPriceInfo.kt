package com.neotelemetrixgdscunand.kakaoxpert.domain.model

data class CocoaDiseaseSellPriceInfo(
    val disease: CocoaDisease,
    val highestPrice: Float,
    val lowestPrice: Float,
    val decreasingRatePerDamageLevel: Float,
)