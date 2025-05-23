package com.neotelemetrixgdscunand.kakaoxpert.domain.model

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

data class CocoaDiseaseSellPriceInfo(
    val disease: CocoaDisease,
    val highestPrice: Float,
    val lowestPrice: Float,
    val decreasingRatePerDamageLevel: Float,
)