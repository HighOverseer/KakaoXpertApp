package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.sensordatadetails

import com.neotelemetrixgdscunand.kakaoxpert.domain.model.SensorItemData
import java.util.Calendar
import kotlin.math.min

fun SensorItemData.getDayInFraction(): Float {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = timeString.toLong()
    val baseDay = calendar[Calendar.DAY_OF_WEEK]
        .minus(1)
        .run {
            if (this <= 0) 7 else this
        }
    val fraction = calendar.run {
        val hour = this[Calendar.HOUR_OF_DAY]
        val minute = this[Calendar.MINUTE]
        val second = this[Calendar.SECOND]

        val totalSecondsInDay = 24 * 60 * 60f
        val totalSecondsInCurrentMoment = (hour * 3600f) + (minute * 60f) + (second)
        totalSecondsInCurrentMoment / totalSecondsInDay
    }
    return baseDay.minus(1).plus(fraction)
        .apply { println("day : $this") }
}

fun getTimeString(additionalTimesInMillis: Long = 0L): String {
    return Calendar.getInstance()
        .apply {
            this[Calendar.DAY_OF_WEEK] = Calendar.MONDAY
            this[Calendar.HOUR_OF_DAY] = 0
            this[Calendar.MINUTE] = 0
            this[Calendar.SECOND] = 0
            this[Calendar.MILLISECOND] = 0
        }
        .timeInMillis
        .plus(additionalTimesInMillis)
        .toString()
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

