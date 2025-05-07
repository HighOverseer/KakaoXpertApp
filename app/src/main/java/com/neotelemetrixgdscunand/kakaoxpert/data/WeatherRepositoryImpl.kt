package com.neotelemetrixgdscunand.kamekapp.data

import com.neotelemetrixgdscunand.kakaoxpert.data.remote.ApiService
import com.neotelemetrixgdscunand.kamekapp.data.utils.fetchFromNetwork
import com.neotelemetrixgdscunand.kamekapp.domain.common.DataError
import com.neotelemetrixgdscunand.kamekapp.domain.common.Result
import com.neotelemetrixgdscunand.kamekapp.domain.common.RootNetworkError
import com.neotelemetrixgdscunand.kamekapp.domain.data.WeatherRepository
import com.neotelemetrixgdscunand.kamekapp.domain.model.WeatherForecastItem
import com.neotelemetrixgdscunand.kamekapp.domain.model.WeatherForecastOverview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val dataMapper: WeatherDtoMapper
) : WeatherRepository {

    override fun getWeatherForecastOverviewAutoUpdate(
        latitude: Double,
        longitude: Double,
        delayEachRequestWhenSuccessMs: Long,
        delayEachRequestWhenErrorMs: Long
    ): Flow<Result<WeatherForecastOverview, DataError.NetworkError>> = flow {
        while (true) {
            val result = fetchFromNetwork {
                val response = apiService.getWeatherForecastOverview(latitude, longitude)
                val weatherForecastOverviewDto = response.data
                    ?: return@fetchFromNetwork Result.Error(RootNetworkError.INTERNAL_SERVER_ERROR)

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
        return fetchFromNetwork {
            val response = apiService.getWeatherForecastForSevenDays(
                latitude, longitude
            )
            val weatherForecastItemDtos = response.data ?: return@fetchFromNetwork Result.Error(
                RootNetworkError.INTERNAL_SERVER_ERROR
            )

            val weatherForecastItems = weatherForecastItemDtos.mapNotNull {
                dataMapper.mapWeatherForecastItemDtoToDomain(it)
            }

            return@fetchFromNetwork Result.Success(weatherForecastItems)
        }
    }
}