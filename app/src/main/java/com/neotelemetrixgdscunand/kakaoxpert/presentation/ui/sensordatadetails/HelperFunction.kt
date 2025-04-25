package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.sensordatadetails

import android.graphics.RectF
import android.graphics.Region
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.asAndroidPath
import androidx.core.graphics.and
import androidx.core.graphics.contains
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.SensorItemData
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.math.abs

fun SensorItemData.getDayInFraction(
    baseDayOfTheYear: Int = 0
): Float {

    val calendar2 = Calendar.getInstance()
    calendar2.timeInMillis = timeInMillis

    val day = calendar2[Calendar.DAY_OF_YEAR]
        .minus(baseDayOfTheYear)

    val dayFraction = calendar2.run {
        val hour = this[Calendar.HOUR_OF_DAY]
        val minute = this[Calendar.MINUTE]
        val second = this[Calendar.SECOND]

        val totalSecondsInDay = 24 * 60 * 60f
        val totalSecondsInCurrentMoment = (hour * 3600f) + (minute * 60f) + (second)
        totalSecondsInCurrentMoment / totalSecondsInDay
    }
    return day.plus(dayFraction)
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


fun isPointInsidePath(path:Path, offset: Offset):Boolean{
    val bounds = path.getBounds()

    val region = Region().apply {
        setPath(path.asAndroidPath(), Region(bounds.left.toInt(), bounds.top.toInt(), bounds.right.toInt(), bounds.bottom.toInt()))
    }

    return region.contains(offset.x.toInt(), offset.y.toInt())
}

suspend fun getOffsetWithMatchingX(
    path: Path,
    touch:Offset,
    precision:Float = 0.5f
):Offset? = coroutineScope{

    val pathMeasure = PathMeasure().apply {
        setPath(path, false)
    }
    val targetX = touch.x

    var nearestOffset:Offset? = null
    var minXDiff = Float.MAX_VALUE

    val pathLength = pathMeasure.length
    var distance = 0f

    ensureActive()

    while (distance <= pathLength){
        val position = pathMeasure.getPosition(distance)
        val diff = abs(position.x - targetX)

        ensureActive()

        if (diff < minXDiff){
            minXDiff = diff
            nearestOffset = position
            if(diff < precision) break
        }

        distance += precision
    }

    nearestOffset
}
