package com.neotelemetrixgdscunand.kakaoxpert.domain.model

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

data class CocoaAverageSellPriceInfo(
    val currentAveragePrice: Float,
    val previousAveragePrice: Float?,
    val rateFromPrevious: Float?,
    val time:Long,
)