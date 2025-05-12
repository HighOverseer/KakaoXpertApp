package com.neotelemetrixgdscunand.kakaoxpert.domain.model

data class WeatherForecastItem(
    val time: Long,
    val maxTemperature: Float,
    val minTemperature: Float,
    val windVelocity: Int,
    val humidity: Int,
    val rainfall: Int,
    val type: WeatherType
)