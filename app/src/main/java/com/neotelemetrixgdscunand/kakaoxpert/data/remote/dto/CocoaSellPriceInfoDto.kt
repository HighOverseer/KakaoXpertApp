package com.neotelemetrixgdscunand.kakaoxpert.data.remote.dto

import com.google.gson.annotations.SerializedName

data class CocoaSellPriceInfoDto(

    @field:SerializedName("latest_disease_price_info")
    val latestDiseasePriceInfo: List<CocoaDiseaseSellPriceInfoDto?> = emptyList(),

    @field:SerializedName("rate_from_previous")
    val rateFromPrevious: Float? = null,

    @field:SerializedName("average_sell_price")
    val averageSellPrice: Float? = null,

    @field:SerializedName("average_sell_price_previous")
    val averageSellPricePrevious: Float? = null
)

