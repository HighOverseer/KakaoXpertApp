package com.neotelemetrixgdscunand.kakaoxpert.data.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("cocoa_average_sell_price_history")
data class CocoaAverageSellPriceHistoryEntity (
    @ColumnInfo("id")
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo("current_average_price")
    val currentAveragePrice: Float,

    @ColumnInfo("previous_average_price")
    val previousAveragePrice: Float?,

    @ColumnInfo("rate_from_previous")
    val rateFromPrevious: Float?,

    @ColumnInfo("time")
    val time:Long,
)