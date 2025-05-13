package com.neotelemetrixgdscunand.kakaoxpert.data.remote

import com.neotelemetrixgdscunand.kakaoxpert.data.remote.dto.AnalysisSessionDto
import com.neotelemetrixgdscunand.kakaoxpert.data.remote.dto.AnalysisSessionPreviewDto
import com.neotelemetrixgdscunand.kakaoxpert.data.remote.dto.NewsDetailsDto
import com.neotelemetrixgdscunand.kakaoxpert.data.remote.dto.NewsItemDto
import com.neotelemetrixgdscunand.kakaoxpert.data.remote.dto.Response
import com.neotelemetrixgdscunand.kakaoxpert.data.remote.dto.ShopItemDto
import com.neotelemetrixgdscunand.kakaoxpert.data.remote.dto.WeatherForecastItemDto
import com.neotelemetrixgdscunand.kakaoxpert.data.remote.dto.WeatherForecastOverviewDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
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