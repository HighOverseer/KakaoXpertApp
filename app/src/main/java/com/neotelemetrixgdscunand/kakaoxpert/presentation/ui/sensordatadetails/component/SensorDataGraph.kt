package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.sensordatadetails.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.copy
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.SensorItemData
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Black10
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Green55
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Grey60
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Grey69
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Grey75
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.KakaoXpertTheme
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Orange85
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.sensordatadetails.computeTouchedPointValue
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.sensordatadetails.getDayInFraction
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.sensordatadetails.getNearestPointOffsetInPathWithMatchingX
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.sensordatadetails.getSevenPreviousDay
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.sensordatadetails.getTimeInMillis
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.Calendar
import kotlin.math.abs
import kotlin.math.roundToInt
import kotlin.random.Random

@Composable
fun SensorDataGraph(
    modifier: Modifier = Modifier,
    sensorItemData: ImmutableList<SensorItemData> = persistentListOf(),
    onProcessSlidingGraphPointer: () -> Unit = { },
    onFinishSlidingGraphPointer: () -> Unit = { },
    onDelegateScroll: (Float) -> Unit = { },
    baseDayOfTheYear: Int = 1,
    isNavigatingUp:Boolean = false
) {


    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
            contentColor = Color.White
        ),
        shape = RoundedCornerShape(8.dp),
        onClick = {}
    ) {

        val textMeasurer = rememberTextMeasurer()
        val textAxisStyle = MaterialTheme.typography.labelMedium.copy(
            textAlign = TextAlign.Center
        )

        val invalidValue = -1
        var lowerBoundDataY by remember { mutableIntStateOf(invalidValue) }
        var upperBoundDataY by remember { mutableIntStateOf(invalidValue) }
        var stepPerUnit by remember { mutableIntStateOf(invalidValue) }

        val lowerBoundDataX = remember(baseDayOfTheYear) {
            Calendar.getInstance()
                .let {
                    it[Calendar.DAY_OF_YEAR] = baseDayOfTheYear
                    it[Calendar.HOUR_OF_DAY] = 0
                    it[Calendar.MINUTE] = 0
                    it[Calendar.SECOND] = 0
                    it[Calendar.MILLISECOND] = 0

                    it.timeInMillis
                }
        }

        val upperBoundDataX = remember {
            Calendar.getInstance()
                .let {
                    it[Calendar.DAY_OF_YEAR] = baseDayOfTheYear + 6
                    it[Calendar.HOUR_OF_DAY] = 23
                    it[Calendar.MINUTE] = 59
                    it[Calendar.SECOND] = 59
                    it[Calendar.MILLISECOND] = 999

                    it.timeInMillis
                }
        }

        val xAxisParametersCount = 7

        val canDraw by remember {
            derivedStateOf {
                lowerBoundDataY != invalidValue
                        && upperBoundDataY != invalidValue
                        && stepPerUnit != invalidValue
            }
        }

        val xTextMeasurables = remember(baseDayOfTheYear) {
            getSevenPreviousDay(baseDayOfTheYear).map {
                textMeasurer.measure(
                    text = it,
                    style = textAxisStyle.copy(
                        textAlign = TextAlign.Center
                    )
                )
            }
        }


        val yTextMeasurables = remember {
            val sensorDataUnit = sensorItemData.firstOrNull()?.unit ?: ""
            val minimumValue = sensorItemData
                .minOfOrNull { it.value }
                ?.roundToInt() ?: 0

            val maximumValue = sensorItemData
                .maxOfOrNull { it.value }
                ?.plus(1)?.roundToInt()
                ?.run {
                    if (minimumValue + 4 > this) minimumValue + 4 else this
                } ?: 0

            stepPerUnit = ((maximumValue - minimumValue) / 4)

            lowerBoundDataY = minimumValue

            (minimumValue..maximumValue step stepPerUnit).map {
                upperBoundDataY = it

                textMeasurer.measure(
                    text = "$it$sensorDataUnit",
                    style = textAxisStyle
                )
            }.toImmutableList()

        }

        var touchedPointInPath by remember {
            mutableStateOf<Offset?>(null)
        }

        var isMovingPointer by remember {
            mutableStateOf(false)
        }

        var dataValuePath = remember<Path?> { null }


        if (canDraw) {
            val coroutineScope = rememberCoroutineScope()
            var job = remember<Job?> { (null) }

            val isNavigatingUpUpdated by rememberUpdatedState(isNavigatingUp)

            val isPointerPopupVisible by remember {
                derivedStateOf {
                    !isMovingPointer && touchedPointInPath != null && !isNavigatingUpUpdated
                }
            }

            var mapHeight by remember { mutableFloatStateOf(0f) }
            val density = LocalDensity.current
            val initialYAxisBottomPaddingDp = 32.dp
            val canvasModifier = remember {
                Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.35f)
                    .padding(16.dp)
                    .onGloballyPositioned {
                        with(density) {
                            mapHeight = it.size.height - initialYAxisBottomPaddingDp.toPx()
                        }

                    }
                    .pointerInput(Unit) {
                        // TRANSIENT STATE
                        var isScrollingBeingDelegatedToParent = false

                        awaitEachGesture {
                            awaitFirstDown()

                            val dataPath = dataValuePath ?: return@awaitEachGesture

                            var currentEventChanges: PointerInputChange
                            var currentRelativePosition: Offset

                            do {
                                val event = awaitPointerEvent()
                                currentEventChanges = event.changes.first()
                                currentRelativePosition = currentEventChanges.position
                                val isChangesInXGreaterThanY =
                                    abs(currentRelativePosition.x - currentEventChanges.previousPosition.x) > abs(
                                        currentRelativePosition.y - currentEventChanges.previousPosition.y
                                    )

                                job?.cancel()
                                if ((isChangesInXGreaterThanY || isMovingPointer) && !isScrollingBeingDelegatedToParent) {
                                    job = coroutineScope.launch {
                                        onProcessSlidingGraphPointer()
                                        val nearestPointInPath =
                                            getNearestPointOffsetInPathWithMatchingX(
                                                dataPath, currentRelativePosition
                                            )
                                        touchedPointInPath = nearestPointInPath
                                        isMovingPointer = true
                                    }
                                } else {
                                    touchedPointInPath = null
                                    onDelegateScroll(currentEventChanges.scrollDelta.y)
                                    isScrollingBeingDelegatedToParent = true
                                    isMovingPointer = false
                                }
                            } while (event.changes.any { it.pressed })

                            if (isMovingPointer) {
                                job?.cancel()
                                job = coroutineScope.launch {
                                    val nearestPointInPath =
                                        getNearestPointOffsetInPathWithMatchingX(
                                            dataPath,
                                            currentRelativePosition
                                        )
                                    touchedPointInPath = nearestPointInPath
                                    isMovingPointer = false
                                }
                            } else touchedPointInPath = null

                            onFinishSlidingGraphPointer()
                            isScrollingBeingDelegatedToParent = false

                        }
                    }
            }

            Canvas(
                modifier = canvasModifier
            ) {
                val contentXWidth = xTextMeasurables[0].size.width
                val contentYHeight = yTextMeasurables[0].size.height

                val initialXAxisStartPadding = 40.dp.toPx()
                val xAxisBottomPadding = 32.dp.toPx()
                val initialYAxisBottomPadding = initialYAxisBottomPaddingDp.toPx()
                val spacePerDay =
                    (size.width - initialXAxisStartPadding) / xAxisParametersCount
                val spacePerStepCelcius =
                    (size.height - initialYAxisBottomPadding) / 5



                xTextMeasurables.forEachIndexed { index, it ->
                    val xLowBound = initialXAxisStartPadding + index * spacePerDay
                    val xHighBound = xLowBound + spacePerDay
                    val x = ((xLowBound + xHighBound) / 2f)

                    drawText(
                        it,
                        color = Grey60,
                        topLeft = Offset(
                            x = x.minus(contentXWidth / 2),
                            y = size.height - xAxisBottomPadding
                        ),
                    )
                }

                yTextMeasurables.forEachIndexed { index, it ->
                    val yLowBound =
                        size.height - (initialYAxisBottomPadding + index * spacePerStepCelcius)
                    val yHighBound = yLowBound - spacePerStepCelcius
                    val y = ((yLowBound + yHighBound) / 2f)

                    drawText(
                        it,
                        color = Grey60,
                        topLeft = Offset(
                            x = 0f,
                            y = y.minus(contentYHeight / 2)
                        )
                    )
                }

                for (index in 0 until xTextMeasurables.lastIndex) {
                    val xLowBound = initialXAxisStartPadding + index * spacePerDay
                    val xHighBound = xLowBound + (2 * spacePerDay)
                    val x = ((xLowBound + xHighBound) / 2f)
                    drawLine(
                        color = Grey75,
                        strokeWidth = 1.dp.toPx(),
                        start = Offset(
                            x = x,
                            y = size.height - xAxisBottomPadding
                        ),
                        end = Offset(
                            x = x,
                            y = 0f
                        )
                    )
                }


                for (index in 0 until yTextMeasurables.lastIndex) {
                    val yLowBound =
                        size.height - (initialYAxisBottomPadding + index * spacePerStepCelcius)
                    val yHighBound = yLowBound - spacePerStepCelcius

                    drawLine(
                        color = Grey75,
                        strokeWidth = 1.dp.toPx(),
                        start = Offset(
                            x = initialXAxisStartPadding,
                            y = yHighBound
                        ),
                        end = Offset(
                            x = size.width,
                            y = yHighBound
                        )
                    )
                }

                var lastX = 0f
                var firstX = 0f
                var maxY = 0f
                val strokePath = Path()
                    .apply {
                        val height = size.height

                        for (i in sensorItemData.indices) {
                            val data = sensorItemData[i]

                            val isDataOutOfBoundInXAxis =
                                data.timeInMillis < lowerBoundDataX || data.timeInMillis > upperBoundDataX
                            val isDataOutOfBoundInYAxis =
                                data.value < lowerBoundDataY.minus(stepPerUnit / 2) || data.value > upperBoundDataY.plus(
                                    stepPerUnit / 2
                                )

                            val isDataOutOfBound =
                                isDataOutOfBoundInYAxis || isDataOutOfBoundInXAxis
                            if (isDataOutOfBound) continue

                            val numerator =
                                (data.value - lowerBoundDataY.toFloat().minus(stepPerUnit / 2f))
                            val denominator = (upperBoundDataY.toFloat()
                                .plus(stepPerUnit / 2f) - lowerBoundDataY.toFloat()
                                .minus(stepPerUnit / 2f))
                            val ratio = numerator / denominator
                            val yValueRatio = (1 - ratio)

                            // 2.dp -> to slide it to the end a little
                            val x1 =
                                initialXAxisStartPadding + data.getDayInFraction(baseDayOfTheYear = baseDayOfTheYear) * spacePerDay + 2.dp.toPx()
                            val y1 = (height - initialYAxisBottomPadding) * yValueRatio

                            if (i == 0) {
                                moveTo(x1, y1)
                                firstX = x1
                            }
                            lastX = x1

                            lineTo(
                                x1,
                                y1,
                            )

                            if (y1 > maxY) maxY = y1
                        }

                    }

                dataValuePath = strokePath

                val fillPath = strokePath.copy()
                    .apply {
                        lineTo(lastX, maxY)
                        lineTo(firstX, maxY)
                        close()
                    }

                drawPath(
                    path = fillPath,
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Green55.copy(
                                alpha = 0.3f
                            ),
                            Color.Transparent
                        ),
                        endY = maxY
                    )
                )

                drawPath(
                    path = strokePath,
                    color = Green55,
                    style = Stroke(
                        width = 2.dp.toPx(),
                        cap = StrokeCap.Round
                    )
                )

                touchedPointInPath?.let {
                    drawPoints(
                        points = listOf(it),
                        color = Orange85,
                        pointMode = PointMode.Points,
                        cap = StrokeCap.Round,
                        strokeWidth = 6.dp.toPx()
                    )
                }
            }

            PointerPopup(
                isVisibleProvider = { isPointerPopupVisible },
                textProvider = {
                    val sensorDataUnit = sensorItemData.firstOrNull()?.unit ?: ""
                    val value = computeTouchedPointValue(
                        lowerBoundDataY = lowerBoundDataY,
                        upperBoundDataY = upperBoundDataY,
                        stepPerUnit = stepPerUnit,
                        maxTouchYCoordinates = mapHeight,
                        touchPointOffsetY = touchedPointInPath?.y?.toInt()
                            ?: return@PointerPopup null
                    )
                    "$value$sensorDataUnit"
                }
            )
        }
    }
}





@Preview
@Composable
private fun SensorDataGraphPreview() {
    KakaoXpertTheme {
        val sortedDescendingXAxis = remember {
            persistentListOf(
                "1\nJan",
                "2\nJan",
                "3\nJan",
                "4\nJan",
                "5\nJan",
                "6\nJan",
                "7\nJan",
            ).toImmutableList()
        }

        val temperatureSensorItemDatas = remember {
            List(168) { index ->
                val randomAdditionalValue = Random.nextInt(-10, 10)
                SensorItemData.Temperature(
                    value = -5f + ((index / 167f) * 50) + randomAdditionalValue,
                    timeInMillis = getTimeInMillis(
                        additionalTimesInMillis = (24f * 3600_000f * (index / 24f)).toLong()
                    )
                )
            }.toImmutableList().apply { this.forEach { println(it) } }
//            listOf(
//                SensorItemData.Temperature(
//                    value = 0.0f,
//                    timeString = getTimeString(
//                        additionalTimesInMillis = (24f * 3600_000f * (0f)).toLong()
//                    )
//                ),
//                SensorItemData.Temperature(
//                    value = 10.0f,
//                    timeString = getTimeString(
//                        additionalTimesInMillis = (24f * 3600_000f * (0f)).toLong()
//                    )
//                ),
//                SensorItemData.Temperature(
//                    value = 20.0f ,
//                    timeString = getTimeString(
//                        additionalTimesInMillis = (24f * 3600_000f * (0f)).toLong()
//                    )
//                ),
//                SensorItemData.Temperature(
//                    value = 10.0f ,
//                    timeString = getTimeString(
//                        additionalTimesInMillis = (24f * 3600_000f * (1f)).toLong()
//                    )
//                ),
//
//            ).toImmutableList()
        }

        val baseDayOfTheYear = remember {
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.DAY_OF_YEAR, -6)
            calendar.get(Calendar.DAY_OF_YEAR)
        }
        Column(
            Modifier
                .fillMaxSize()
                .background(color = Grey69)
                .padding(16.dp)
        ) {
            SensorDataGraph(
                sensorItemData = temperatureSensorItemDatas,
                baseDayOfTheYear = baseDayOfTheYear
            )
        }

    }
}


@Composable
private fun SensorDataGraphTest(
    modifier: Modifier = Modifier,
    sortedDescendingXAxis: ImmutableList<String> = persistentListOf(),
    sortedDescendingYAxis: ImmutableList<String> = persistentListOf(),
    sensorItemData: ImmutableList<SensorItemData> = persistentListOf()
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(8.dp),
        onClick = {}
    ) {

        val textMeasurer = rememberTextMeasurer()
        val textAxisStyle = MaterialTheme.typography.labelMedium
        val xTextMeasurables = remember {
            sortedDescendingXAxis.map {
                textMeasurer.measure(
                    text = it,
                    style = textAxisStyle.copy(
                        textAlign = TextAlign.Center
                    )
                )
            }.toImmutableList()
        }
        val yTextMeasurables = remember {
            sortedDescendingYAxis.map {
                textMeasurer.measure(
                    text = it,
                    style = textAxisStyle
                )
            }.toImmutableList()
        }

        val baseDayOfTheYear = remember {
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.DAY_OF_YEAR, -6)
            calendar.get(Calendar.DAY_OF_YEAR)
        }

        Canvas(
            Modifier
                .fillMaxWidth()
                .aspectRatio(1.35f)
                .padding(16.dp)
        ) {
            val xPadding = 20.dp.toPx()
            val yPadding = 0.dp.toPx()
            val xSpacing = 40.dp.toPx()
            val ySpacing = 48.dp.toPx()
            val spacePerDay =
                (size.width - xSpacing - xPadding) / sortedDescendingXAxis.size.minus(1)
            val spacePerTenCelcius =
                (size.height - ySpacing - yPadding) / sortedDescendingYAxis.size.minus(1)
            xTextMeasurables.forEachIndexed { index, it ->
                drawText(
                    it,
                    color = Grey60,
                    topLeft = Offset(
                        x = xSpacing + index * spacePerDay,
                        y = size.height - 32.dp.toPx()
                    ),
                )
            }

            yTextMeasurables.forEachIndexed { index, it ->
                drawText(
                    it,
                    color = Grey60,
                    topLeft = Offset(
                        x = 0f,
                        y = size.height - (ySpacing + index * spacePerTenCelcius)
                    ),
                )
            }

            for (index in 0 until xTextMeasurables.lastIndex) {
                drawLine(
                    color = Grey75,
                    strokeWidth = 1.dp.toPx(),
                    start = Offset(
                        x = xSpacing + ((index + 0.75f) * spacePerDay),
                        y = size.height - 32.dp.toPx()
                    ),
                    end = Offset(
                        x = xSpacing + ((index + 0.75f) * spacePerDay),
                        y = yPadding
                    )
                )
            }

            var lastX = 0f
            val strokePath = Path()
                .apply {
                    val height = size.height

                    for (i in sensorItemData.indices) {
                        val data = sensorItemData[i]
                        val nextData = sensorItemData.getOrNull(i + 1) ?: sensorItemData.last()

                        val leftRatio = 1 - ((data.value - 0) / (40f - 0f))
                        val rightRatio = 1 - ((nextData.value - 0) / (40f - 0f))

                        val spacePerDay2 =
                            ((size.width - xSpacing - xPadding) / sortedDescendingXAxis.size.minus(1))
                        val x1 =
                            data.getDayInFraction(baseDayOfTheYear) * spacePerDay2 + 6.dp.toPx()
                        //println("day.getday : ${data.getDay()}")
                        val y1 = (height - 32.dp.toPx() - 15.dp.toPx()) * leftRatio + 8.dp.toPx()
                        val x2 =
                            nextData.getDayInFraction(baseDayOfTheYear) * spacePerDay2 + 6.dp.toPx()
                        val y2 = (height - 32.dp.toPx() - 15.dp.toPx()) * rightRatio + 8.dp.toPx()
//                        println(
//                            "x1 : $x1, y1 : $y1, x2 : $x2, y2 : $y2"
//                        )

                        if (i == 0) {
                            println(
                                "move x1 : $x1, y1 : $y1\n day : ${data.getDayInFraction()}"
                            )
                            moveTo(x1, y1)
                        }
                        lastX = (x1 + x2) / 2f
                        //if aesthetic or curve needed, use this instead, but will not be so precise especially when data is few
//                        quadraticTo(
//                            x1,
//                            y1,
//                            lastX,
//                            (y1 + y2) / 2f
//                        )

                        //if precision is the goal, use this instead, better when data is few
                        lineTo(
                            x1,
                            y1,
                        )
                    }
                }

            sensorItemData.map {
                val leftRatio = 1 - ((it.value - 0) / (40f - 0f))
                val spacePerDay2 =
                    ((size.width - xSpacing - xPadding) / sortedDescendingXAxis.size.minus(1))
                val x = it.getDayInFraction() * spacePerDay2 + 6.dp.toPx()

                // 15.dp and 8.dp in the code below, is random number after some testing, to keep the horizontal line of each point exacly match the y axis
                val y = ((size.height - 32.dp.toPx() - 15.dp.toPx()) * leftRatio) + 8.dp.toPx()
                println(
                    "x1 : $x, y1 : $y"
                )
                Offset(x = x, y = y)
            }.apply {
                this.forEach {
                    drawLine(
                        color = Black10,
                        strokeWidth = 1.dp.toPx(),
                        start = Offset(
                            x = it.x,
                            y = this@Canvas.size.height
                        ),
                        end = Offset(
                            x = it.x,
                            y = 0f
                        )
                    )

                    drawLine(
                        color = Black10,
                        strokeWidth = 1.dp.toPx(),
                        start = Offset(
                            x = 0f,
                            y = it.y
                        ),
                        end = Offset(
                            x = this@Canvas.size.width,
                            y = it.y
                        )
                    )
                }
                drawPoints(
                    points = this,
                    pointMode = PointMode.Points,
                    color = Black10,
                    strokeWidth = 5.dp.toPx(),
                    cap = StrokeCap.Round
                )
            }

            val fillPath = strokePath.copy()
                .apply {
                    val height = size.height

                    lineTo(lastX, height - 50f)
                    lineTo(xSpacing * (1.25f), height - 50f)
                    close()
                }

            drawPath(
                path = fillPath,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Green55.copy(
                            alpha = 0.3f
                        ),
                        Color.Transparent
                    ),
                    endY = size.height - 50f
                )
            )

            drawPath(
                path = strokePath,
                color = Green55,
                style = Stroke(
                    width = 2.dp.toPx(),
                    cap = StrokeCap.Round
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SensorDataGraphTestPreview() {
    KakaoXpertTheme {
        val sortedDescendingYAxis = remember {
            persistentListOf(
                "40°C",
                "30°C",
                "20°C",
                "10°C",
                "0°C",
            ).reversed().toImmutableList()
        }
        val sortedDescendingXAxis = remember {
            persistentListOf(
                "1\nJan",
                "2\nJan",
                "3\nJan",
                "4\nJan",
                "5\nJan",
                "6\nJan",
                "7\nJan",
            ).toImmutableList()
        }

        val temperatureSensorItemDatas = remember {
//            List(7){ index ->
//                val randomAdditionalValue = Random.nextInt(0, 30)
//                SensorItemData.Temperature(
//                    value = 10.0f + index,
//                    timeString = getTimeString(
//                        additionalTimesInMillis = 3600_000L * 24 * (index)
//                    )
//                )
//            }.toImmutableList()
            listOf(
                SensorItemData.Temperature(
                    value = 10.0f,
                    timeInMillis = getTimeInMillis(
                        additionalTimesInMillis = 3600_000L * 24 * (0)
                    )
                ), SensorItemData.Temperature(
                    value = 15.0f,
                    timeInMillis = getTimeInMillis(
                        additionalTimesInMillis = 3600_000L * 24 * (1)
                    )
                ), SensorItemData.Temperature(
                    value = 20.0f,
                    timeInMillis = getTimeInMillis(
                        additionalTimesInMillis = 3600_000L * 24 * (2)
                    )
                ), SensorItemData.Temperature(
                    value = 25.0f,
                    timeInMillis = getTimeInMillis(
                        additionalTimesInMillis = 3600_000L * 24 * (3)
                    )
                ), SensorItemData.Temperature(
                    value = 30.0f,
                    timeInMillis = getTimeInMillis(
                        additionalTimesInMillis = 3600_000L * 24 * (4)
                    )
                ), SensorItemData.Temperature(
                    value = 35.0f,
                    timeInMillis = getTimeInMillis(
                        additionalTimesInMillis = 3600_000L * 24 * (5)
                    )
                ), SensorItemData.Temperature(
                    value = 40.0f,
                    timeInMillis = getTimeInMillis(
                        additionalTimesInMillis = 3600_000L * 24 * (6)
                    )
                )
            ).toImmutableList()
        }
        Column(
            Modifier
                .fillMaxSize()
                .background(color = Grey69)
                .padding(16.dp)
        ) {
            SensorDataGraphTest(
                sortedDescendingXAxis = sortedDescendingXAxis,
                sortedDescendingYAxis = sortedDescendingYAxis,
                sensorItemData = temperatureSensorItemDatas
            )
        }

    }
}