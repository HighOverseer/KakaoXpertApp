package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.weather

import com.neotelemetrixgdscunand.kakaoxpert.presentation.utils.UIText

sealed interface WeatherUIEvent {
    data class OnLocationResolvableError(val exception: Exception) : WeatherUIEvent
    data class OnLocationUnknownError(val errorUIText: UIText) : WeatherUIEvent
    data class OnFailedFetchWeatherForecast(val errorUIText: UIText) : WeatherUIEvent
}