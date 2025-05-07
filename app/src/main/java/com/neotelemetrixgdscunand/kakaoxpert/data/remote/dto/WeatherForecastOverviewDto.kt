package com.neotelemetrixgdscunand.kamekapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class WeatherForecastOverviewDto(

    @field:SerializedName("max_temperature")
    val maxTemperature: Float? = null,

    @field:SerializedName("current_temperature")
    val currentTemperature: Float? = null,

    @field:SerializedName("wind_velocity")
    val windVelocity: Float? = null,

    @field:SerializedName("rainfall")
    val rainfall: Int? = null,

    @field:SerializedName("humidity")
    val humidity: Int? = null,

    @field:SerializedName("type_id")
    val typeId: Int? = null,

    @field:SerializedName("min_temperature")
    val minTemperature: Float? = null
)
