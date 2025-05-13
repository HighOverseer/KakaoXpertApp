package com.neotelemetrixgdscunand.kakaoxpert.data.remote

import com.neotelemetrixgdscunand.kakaoxpert.data.remote.dto.Response
import com.neotelemetrixgdscunand.kakaoxpert.data.remote.dto.WeatherForecastItemDto
import com.neotelemetrixgdscunand.kakaoxpert.data.remote.dto.WeatherForecastOverviewDto
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    @GET("weather-home")
    suspend fun getWeatherForecastOverview(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double
    ): Response<WeatherForecastOverviewDto>

    @GET("weather")
    suspend fun getWeatherForecastForSevenDays(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double
    ): Response<List<WeatherForecastItemDto>>
}