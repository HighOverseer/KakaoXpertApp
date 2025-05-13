package com.neotelemetrixgdscunand.kakaoxpert.data

import com.neotelemetrixgdscunand.kakaoxpert.data.remote.WeatherApiService
import com.neotelemetrixgdscunand.kakaoxpert.data.utils.callApiFromNetwork
import com.neotelemetrixgdscunand.kakaoxpert.domain.common.DataError
import com.neotelemetrixgdscunand.kakaoxpert.domain.common.Result
import com.neotelemetrixgdscunand.kakaoxpert.domain.common.RootNetworkError
import com.neotelemetrixgdscunand.kakaoxpert.domain.data.WeatherRepository
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.WeatherForecastItem
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.WeatherForecastOverview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepositoryImpl @Inject constructor(
    private val weatherApiService: WeatherApiService,
    private val dataMapper: WeatherDtoMapper
) : WeatherRepository {

    override fun getWeatherForecastOverviewAutoUpdate(
        latitude: Double,
        longitude: Double,
        delayEachRequestWhenSuccessMs: Long,
        delayEachRequestWhenErrorMs: Long
    ): Flow<Result<WeatherForecastOverview, DataError.NetworkError>> = flow {
        while (true) {
            val result = callApiFromNetwork {
                val response = weatherApiService.getWeatherForecastOverview(latitude, longitude)
                val weatherForecastOverviewDto = response.data
                    ?: return@callApiFromNetwork Result.Error(RootNetworkError.INTERNAL_SERVER_ERROR)

                val weatherForecastOverview =
                    dataMapper.mapWeatherForecastOverviewDtoToDomain(weatherForecastOverviewDto)

                Result.Success(weatherForecastOverview)
            }
            emit(result)

            val delayMs = if (result is Result.Success) {
                delayEachRequestWhenSuccessMs
            } else delayEachRequestWhenErrorMs

            delay(delayMs)
        }
    }

    override suspend fun getWeatherForecastForSeveralDays(
        latitude: Double,
        longitude: Double
    ): Result<List<WeatherForecastItem>, DataError.NetworkError> {
        return callApiFromNetwork {
            val response = weatherApiService.getWeatherForecastForSevenDays(
                latitude, longitude
            )
            val weatherForecastItemDtos = response.data ?: return@callApiFromNetwork Result.Error(
                RootNetworkError.INTERNAL_SERVER_ERROR
            )

            val weatherForecastItems = weatherForecastItemDtos.mapNotNull {
                dataMapper.mapWeatherForecastItemDtoToDomain(it)
            }

            return@callApiFromNetwork Result.Success(weatherForecastItems)
        }
    }
}