package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.toplevel.util

import com.neotelemetrixgdscunand.kakaoxpert.domain.model.SensorItemData
import java.util.Calendar

fun SensorItemData.getDay(): Int {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = timeString.toLong()
    return calendar[Calendar.DAY_OF_WEEK]
        .minus(1)
        .run {
            if (this <= 0) 7 else this
        }
}

fun getTimeString(additionalTimesInMillis: Long = 0L): String {
    return Calendar.getInstance()
        .apply {
            this[Calendar.DAY_OF_WEEK] = Calendar.MONDAY
        }
        .timeInMillis
        .plus(additionalTimesInMillis)
        .toString()
}