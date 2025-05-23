package com.neotelemetrixgdscunand.kakaoxpert.data.remote.dto

import com.google.gson.annotations.SerializedName

data class CocoaDiseaseSellPriceInfoDto(
	@field:SerializedName("disease_id")
	val diseaseId: Int? = null,

	@field:SerializedName("sell_price_decreasing_rate_per_one_percent_damage_level")
	val sellPriceDecreasingRatePerOnePercentDamageLevel: Float? = null,

	@field:SerializedName("lowest_price")
	val lowestPrice: Float? = null,

	@field:SerializedName("highest_price")
	val highestPrice: Float? = null
)
