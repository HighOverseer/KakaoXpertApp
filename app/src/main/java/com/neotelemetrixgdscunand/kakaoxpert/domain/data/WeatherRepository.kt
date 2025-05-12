package com.neotelemetrixgdscunand.kakaoxpert.domain.data

import com.neotelemetrixgdscunand.kakaoxpert.domain.common.DataError
import com.neotelemetrixgdscunand.kakaoxpert.domain.common.Result
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.WeatherForecastItem
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.WeatherForecastOverview
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    fun getWeatherForecastOverviewAutoUpdate(
        latitude: Double,
        longitude: Double,
        delayEachRequestWhenSuccessMs: Long = DELAY_EACH_SUCCESS_REQUEST_MS,
        delayEachRequestWhenErrorMs: Long = DELAY_EACH_ERROR_REQUEST_MS
    ): Flow<Result<WeatherForecastOverview, DataError.NetworkError>>

    suspend fun getWeatherForecastForSeveralDays(
        latitude: Double,
        longitude: Double
    ): Result<List<WeatherForecastItem>, DataError.NetworkError>

    companion object {
        private const val DELAY_EACH_SUCCESS_REQUEST_MS = 5 * 60 * 1000L // 5 Minute
        private const val DELAY_EACH_ERROR_REQUEST_MS = 10 * 1000L // 10 Second

    }
}