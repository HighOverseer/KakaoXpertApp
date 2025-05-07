package com.neotelemetrixgdscunand.kamekapp.presentation.dui

import com.neotelemetrixgdscunand.kamekapp.presentation.utils.UIText


data class WeatherForecastOverviewDui(
    val maxTemperature: UIText,
    val minTemperature: UIText,
    val currentTemperature: UIText,
    val windVelocity: UIText,
    val humidity: UIText,
    val rainfall: UIText,
    val name: UIText,
    val iconResourceId: Int,
)