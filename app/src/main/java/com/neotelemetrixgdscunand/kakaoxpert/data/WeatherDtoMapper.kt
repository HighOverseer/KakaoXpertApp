package com.neotelemetrixgdscunand.kamekapp.data

import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.diagnosisresult.util.roundOffDecimal
import com.neotelemetrixgdscunand.kamekapp.data.remote.dto.WeatherForecastItemDto
import com.neotelemetrixgdscunand.kamekapp.data.remote.dto.WeatherForecastOverviewDto
import com.neotelemetrixgdscunand.kamekapp.domain.model.WeatherForecastItem
import com.neotelemetrixgdscunand.kamekapp.domain.model.WeatherForecastOverview
import com.neotelemetrixgdscunand.kamekapp.domain.model.WeatherType
import kotlinx.coroutines.CancellationException
import java.text.SimpleDateFormat
import java.util.Locale

object WeatherDtoMapper {

    private const val DTO_DATE_PATTERN = "yyyy-MM-dd"

    fun mapWeatherForecastItemDtoToDomain(
        weatherForecastItemDto: WeatherForecastItemDto
    ): WeatherForecastItem? {
        val sdf = SimpleDateFormat(DTO_DATE_PATTERN, Locale.getDefault())

        val time = try {
            val date = weatherForecastItemDto.date ?: throw Exception()
            sdf.parse(date)
        } catch (e: Exception) {
            if (e is CancellationException) throw e

            null
        }

        return if (time == null) null else WeatherForecastItem(
            time = time.time,
            maxTemperature = weatherForecastItemDto.maxTemperature?.roundOffDecimal(1) ?: 0f,
            minTemperature = weatherForecastItemDto.minTemperature?.roundOffDecimal(1) ?: 0f,
            windVelocity = weatherForecastItemDto.windVelocity?.toInt() ?: 0,
            humidity = weatherForecastItemDto.humidity?.toInt() ?: 0,
            rainfall = weatherForecastItemDto.rainfall?.toInt() ?: 0,
            type = mapWeatherTypeIdToWeatherType(weatherForecastItemDto.typeId ?: 0)
        )
    }

    fun mapWeatherForecastOverviewDtoToDomain(
        weatherForecastOverviewDto: WeatherForecastOverviewDto
    ): WeatherForecastOverview {
        return WeatherForecastOverview(
            maxTemperature = weatherForecastOverviewDto.maxTemperature?.roundOffDecimal(1) ?: 0f,
            minTemperature = weatherForecastOverviewDto.minTemperature?.roundOffDecimal(1) ?: 0f,
            currentTemperature = weatherForecastOverviewDto.currentTemperature?.toInt() ?: 0,
            windVelocity = weatherForecastOverviewDto.windVelocity?.toInt() ?: 0,
            humidity = weatherForecastOverviewDto.humidity ?: 0,
            rainfall = weatherForecastOverviewDto.rainfall ?: 0,
            type = mapWeatherTypeIdToWeatherType(weatherForecastOverviewDto.typeId ?: 0)
        )
    }

    //The numbers refers to the Open Weather API Documentation
    private fun mapWeatherTypeIdToWeatherType(weatherTypeId: Int): WeatherType {
        return when (weatherTypeId) {
            in 200..232 -> WeatherType.THUNDERSTORM
            in 300..321 -> WeatherType.DRIZZLE
            500, 501 -> WeatherType.DRIZZLE
            in 502..531 -> WeatherType.HEAVY_RAIN
            in 600..622 -> WeatherType.HEAVY_RAIN // Should Be Snow, but no ui design is available
            in 701..781 -> WeatherType.CLOUDY // Should Be Fog, but no ui design is available
            800 -> WeatherType.CLEAR
            801, 802 -> WeatherType.MOSTLY_CLOUDY
            803, 804 -> WeatherType.CLOUDY
            else -> WeatherType.CLEAR
        }
    }
}