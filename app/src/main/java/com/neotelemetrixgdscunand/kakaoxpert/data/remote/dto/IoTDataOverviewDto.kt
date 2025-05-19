package com.neotelemetrixgdscunand.kakaoxpert.data.remote.dto

import com.google.gson.annotations.SerializedName

data class IoTDataOverviewDto(

	@field:SerializedName("humidity_value")
	val humidityValue: Float? = null,

	@field:SerializedName("temperature_value")
	val temperatureValue: Float? = null,

	@field:SerializedName("light_intensity_value")
	val lightIntensityValue: Float? = null
)
