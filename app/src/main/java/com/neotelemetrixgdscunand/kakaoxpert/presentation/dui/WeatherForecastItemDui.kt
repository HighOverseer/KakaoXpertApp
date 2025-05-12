package com.neotelemetrixgdscunand.kakaoxpert.presentation.dui

import com.neotelemetrixgdscunand.kakaoxpert.presentation.utils.UIText

data class WeatherForecastItemDui(
    val date: UIText,
    val maxTemperature: UIText,
    val minTemperature: UIText,
    val windVelocity: UIText,
    val humidity: UIText,
    val iconResourceId: Int,
)