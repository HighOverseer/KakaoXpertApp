package com.neotelemetrixgdscunand.kakaoxpert.data.remote.dto

import com.google.gson.annotations.SerializedName

data class IoTDataDto(

    @field:SerializedName("date")
    val date: String? = null,

    @field:SerializedName("humidity_value")
    val humidityValue: Float? = null,

    @field:SerializedName("temperature_value")
    val temperatureValue: Float? = null,

    @field:SerializedName("light_intensity_value")
    val lightIntensityValue: Float? = null
)
