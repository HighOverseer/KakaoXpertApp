package com.neotelemetrixgdscunand.kamekapp.presentation.ui.weather

import com.neotelemetrixgdscunand.kamekapp.presentation.utils.UIText

sealed interface WeatherUIEvent {
    data class OnLocationResolvableError(val exception: Exception) : WeatherUIEvent
    data class OnLocationUnknownError(val errorUIText: UIText) : WeatherUIEvent
    data class OnFailedFetchWeatherForecast(val errorUIText: UIText) : WeatherUIEvent
}