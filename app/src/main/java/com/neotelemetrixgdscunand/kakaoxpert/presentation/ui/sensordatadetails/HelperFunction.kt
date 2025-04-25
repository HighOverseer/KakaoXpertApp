package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.sensordatadetails

import com.neotelemetrixgdscunand.kakaoxpert.domain.model.SensorItemData
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

fun SensorItemData.getDayInFraction(): Float {
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.DAY_OF_YEAR, -6)
    val baseDayOfTheYear = calendar[Calendar.DAY_OF_YEAR]

    val calendar2 = Calendar.getInstance()
    calendar2.timeInMillis = timeInMillis

    val baseDay = calendar2[Calendar.DAY_OF_YEAR]
        .minus(baseDayOfTheYear)

    val fraction = calendar2.run {
        val hour = this[Calendar.HOUR_OF_DAY]
        val minute = this[Calendar.MINUTE]
        val second = this[Calendar.SECOND]

        val totalSecondsInDay = 24 * 60 * 60f
        val totalSecondsInCurrentMoment = (hour * 3600f) + (minute * 60f) + (second)
        totalSecondsInCurrentMoment / totalSecondsInDay
    }
    return baseDay.plus(fraction)
}

fun getTimeInMillis(additionalTimesInMillis: Long = 0L): Long {
    return Calendar.getInstance()
        .apply {
            this.add(Calendar.DAY_OF_YEAR, -6)
            this[Calendar.HOUR_OF_DAY] = 0
            this[Calendar.MINUTE] = 0
            this[Calendar.SECOND] = 0
            this[Calendar.MILLISECOND] = 0
        }
        .timeInMillis
        .plus(additionalTimesInMillis)
}

fun getSevenPreviousDay():List<String>{
    val df = SimpleDateFormat("d\nMMM", Locale.getDefault())
    val list = mutableListOf<String>()
    val calendar = Calendar.getInstance()

    (0..6).map{ index ->
        val subtractor = if(index > 0) -1 else 0

        calendar.add(Calendar.DAY_OF_YEAR, subtractor)
        val dateString = df.format(calendar.time)
        list.add(dateString)
    }
    return list.reversed()
}

fun getCurrentDayInMillisSeconds(): String {
    return Calendar.getInstance()
        .apply {
            this[Calendar.DAY_OF_WEEK] = Calendar.MONDAY
            this[Calendar.HOUR_OF_DAY] = 0
            this[Calendar.MINUTE] = 0
            this[Calendar.SECOND] = 0
            this[Calendar.MILLISECOND] = 0
        }
        .timeInMillis
        .toString()
}

