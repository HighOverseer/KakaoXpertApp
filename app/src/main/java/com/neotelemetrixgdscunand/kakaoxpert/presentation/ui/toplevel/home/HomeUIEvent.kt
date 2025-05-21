package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.toplevel.home

import com.neotelemetrixgdscunand.kakaoxpert.presentation.utils.UIText

sealed interface HomeUIEvent {
    data class OnLocationResolvableError(val exception: Exception) : HomeUIEvent
    data class OnLocationUnknownError(val errorUIText: UIText) : HomeUIEvent
    data class OnFailedFetchWeatherForecast(val errorUIText: UIText) : HomeUIEvent
    data class OnFailedFetchNewsItems(val errorUIText: UIText) : HomeUIEvent
    data class OnFailedGetIoTDataOverview(val errorUIText: UIText) : HomeUIEvent
}