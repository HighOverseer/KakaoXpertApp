package com.neotelemetrixgdscunand.kakaoxpert.presentation.mapper

import com.neotelemetrixgdscunand.kakaoxpert.R
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.WeatherForecastItem
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.WeatherForecastOverview
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.WeatherType
import com.neotelemetrixgdscunand.kakaoxpert.presentation.dui.WeatherForecastItemDui
import com.neotelemetrixgdscunand.kakaoxpert.presentation.dui.WeatherForecastOverviewDui
import com.neotelemetrixgdscunand.kakaoxpert.presentation.utils.UIText
import kotlinx.coroutines.CancellationException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.math.roundToInt

object WeatherDuiMapper {

    private const val DUI_DATE_PATTERN = "EEE dd"

    fun mapWeatherForecastItemToDui(
        weatherForecastItem: WeatherForecastItem
    ): WeatherForecastItemDui? {

        val sdf = SimpleDateFormat(DUI_DATE_PATTERN, Locale.getDefault())
        val date = try {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = weatherForecastItem.time
            val dateString = sdf.format(calendar.time)

            UIText.DynamicString(dateString)
        } catch (e: Exception) {
            if (e is CancellationException) throw e

            null
        }

        val maxTemperature = UIText.StringResource(
            R.string.suhu_format,
            arrayOf(weatherForecastItem.maxTemperature.roundToInt().toString())
        )
        val minTemperature = UIText.StringResource(
            R.string.suhu_format,
            arrayOf(weatherForecastItem.minTemperature.roundToInt().toString())
        )
        val windVelocity = UIText.StringResource(
            R.string.wind_velocity_format,
            arrayOf(weatherForecastItem.windVelocity.toString())
        )
        val humidity =
            UIText.StringResource(R.string.humidity_format, arrayOf(weatherForecastItem.humidity))
        val iconResId = mapWeatherTypeToIconResourceId(weatherForecastItem.type)

        return if (date == null) null else WeatherForecastItemDui(
            date = date,
            maxTemperature = maxTemperature,
            minTemperature = minTemperature,
            windVelocity = windVelocity,
            humidity = humidity,
            iconResourceId = iconResId
        )
    }

    fun mapWeatherForecastOverviewToDui(
        weatherForecastOverview: WeatherForecastOverview
    ): WeatherForecastOverviewDui {
        val maxTemperature = UIText.StringResource(
            R.string.suhu_format,
            arrayOf(weatherForecastOverview.maxTemperature.toString())
        )
        val minTemperature = UIText.StringResource(
            R.string.suhu_format,
            arrayOf(weatherForecastOverview.minTemperature.toString())
        )
        val currentTemperature = UIText.StringResource(
            R.string.suhu_format,
            arrayOf(weatherForecastOverview.currentTemperature.toString())
        )
        val windVelocity = UIText.StringResource(
            R.string.wind_velocity_format,
            arrayOf(weatherForecastOverview.windVelocity.toString())
        )
        val humidity = UIText.StringResource(
            R.string.humidity_format,
            arrayOf(weatherForecastOverview.humidity)
        )
        val rainFall = UIText.StringResource(
            R.string.rainfall_format,
            arrayOf(weatherForecastOverview.rainfall.toString())
        )
        val name =
            UIText.StringResource(mapWeatherTypeToStringResource(weatherForecastOverview.type))
        val iconResId = mapWeatherTypeToIconResourceId(weatherForecastOverview.type)

        return WeatherForecastOverviewDui(
            maxTemperature = maxTemperature,
            minTemperature = minTemperature,
            currentTemperature = currentTemperature,
            windVelocity = windVelocity,
            humidity = humidity,
            rainfall = rainFall,
            name = name,
            iconResourceId = iconResId
        )
    }

    private fun mapWeatherTypeToStringResource(
        weatherType: WeatherType
    ): Int {
        return when (weatherType) {
            WeatherType.CLOUDY -> R.string.cuaca_berawan
            WeatherType.CLEAR -> R.string.cuaca_cerah
            WeatherType.HEAVY_RAIN -> R.string.cuaca_hujan_lebat
            WeatherType.MOSTLY_CLOUDY -> R.string.cuaca_sebagian_besar_berawan
            WeatherType.THUNDERSTORM -> R.string.cuaca_badai_petir
            WeatherType.DRIZZLE -> R.string.cuaca_hujan_gerimis
        }
    }

    private fun mapWeatherTypeToIconResourceId(
        weatherType: WeatherType
    ): Int {
        return when (weatherType) {
            WeatherType.CLOUDY -> R.drawable.ic_weather_cloudy
            WeatherType.CLEAR -> R.drawable.ic_weather_clear
            WeatherType.HEAVY_RAIN -> R.drawable.ic_weather_heavy_rain
            WeatherType.MOSTLY_CLOUDY -> R.drawable.ic_weather_mostly_cloudy
            WeatherType.THUNDERSTORM -> R.drawable.ic_weather_thunderstorm
            WeatherType.DRIZZLE -> R.drawable.ic_weather_drizzle
        }
    }


}