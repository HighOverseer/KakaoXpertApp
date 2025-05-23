package com.neotelemetrixgdscunand.kakaoxpert.data.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("cocoa_disease_sell_price_info")
data class CocoaDiseaseSellPriceInfoEntity(
    @ColumnInfo("disease_id")
    @PrimaryKey(autoGenerate = false)
    val diseaseId: Int,

    @ColumnInfo("highest_price")
    val highestPrice: Float,

    @ColumnInfo("lowest_price")
    val lowestPrice: Float,

    @ColumnInfo("decreasing_rate_per_damage_level")
    val decreasingRatePerDamageLevel: Float,
)