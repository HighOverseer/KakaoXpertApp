package com.neotelemetrixgdscunand.kakaoxpert.data.remote.dto

import com.google.gson.annotations.SerializedName

data class WeatherForecastItemDto(

    @field:SerializedName("date")
    val date: String? = null,

    @field:SerializedName("max_temperature")
    val maxTemperature: Float? = null,

    @field:SerializedName("wind_velocity")
    val windVelocity: Float? = null,

    @field:SerializedName("rainfall")
    val rainfall: Float? = null,

    @field:SerializedName("humidity")
    val humidity: Float? = null,

    @field:SerializedName("type_id")
    val typeId: Int? = null,

    @field:SerializedName("min_temperature")
    val minTemperature: Float? = null
)
