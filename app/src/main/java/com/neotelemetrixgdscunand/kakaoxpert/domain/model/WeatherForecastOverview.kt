package com.neotelemetrixgdscunand.kamekapp.domain.model

data class WeatherForecastOverview(
    val maxTemperature: Float,
    val minTemperature: Float,
    val currentTemperature: Int,
    val windVelocity: Int,
    val humidity: Int,
    val rainfall: Int,
    val type: WeatherType
)