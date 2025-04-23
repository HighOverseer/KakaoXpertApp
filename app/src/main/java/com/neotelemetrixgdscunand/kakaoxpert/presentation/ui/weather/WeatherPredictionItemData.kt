package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.weather

data class WeatherPredictionItemData(
    val id: Int,
    val date: String,
    val temperatureRange: String,
    val humidityPercentage: Int,
    val windVelocity: Int
)

fun getDummyWeatherPredictionItemData(): List<WeatherPredictionItemData> {
    return List(7) {
        WeatherPredictionItemData(
            it,
            "Sen 21",
            "31°/24°",
            30,
            2
        )
    }
}