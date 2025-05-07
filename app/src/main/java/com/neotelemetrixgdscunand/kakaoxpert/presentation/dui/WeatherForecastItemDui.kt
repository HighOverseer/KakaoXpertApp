package com.neotelemetrixgdscunand.kamekapp.presentation.dui

import com.neotelemetrixgdscunand.kamekapp.presentation.utils.UIText

data class WeatherForecastItemDui(
    val date: UIText,
    val maxTemperature: UIText,
    val minTemperature: UIText,
    val windVelocity: UIText,
    val humidity: UIText,
    val iconResourceId: Int,
)