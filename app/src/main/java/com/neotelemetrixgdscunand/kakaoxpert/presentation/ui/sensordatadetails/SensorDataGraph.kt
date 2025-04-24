package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.sensordatadetails

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.copy
import androidx.compose.ui.graphics.drawscope.Stroke
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
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.toplevel.util.getDay
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.toplevel.util.getTimeString
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlin.random.Random

@Composable
fun SensorDataGraph(
    modifier: Modifier = Modifier,
    sortedDescendingXAxis: ImmutableList<String> = persistentListOf(),
    sortedDescendingYAxis: ImmutableList<String> = persistentListOf(),
    sensorItemData: ImmutableList<SensorItemData> = persistentListOf()
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

        Canvas(
            Modifier
                .fillMaxWidth()
                .aspectRatio(1.35f)
                .padding(16.dp)
        ) {
            val xAxisEndPadding = 20.dp.toPx()
            val yAxisTopPadding = 0.dp.toPx()
            val initialXAxisStartPadding = 40.dp.toPx()
            val xAxisBottomPadding = 32.dp.toPx()
            val initialYAxisBottomPadding = 48.dp.toPx()
            val spacePerDay =
                (size.width - initialXAxisStartPadding - xAxisEndPadding) / sortedDescendingXAxis.size.minus(
                    1
                )
            val spacePerTenCelcius =
                (size.height - initialYAxisBottomPadding - yAxisTopPadding) / sortedDescendingYAxis.size.minus(
                    1
                )
            xTextMeasurables.forEachIndexed { index, it ->
                drawText(
                    it,
                    color = Grey60,
                    topLeft = Offset(
                        x = initialXAxisStartPadding + index * spacePerDay,
                        y = size.height - xAxisBottomPadding
                    ),
                )
            }

            yTextMeasurables.forEachIndexed { index, it ->
                drawText(
                    it,
                    color = Grey60,
                    topLeft = Offset(
                        x = 0f,
                        y = size.height - (initialYAxisBottomPadding + index * spacePerTenCelcius)
                    ),
                )
            }

            for (index in 0 until xTextMeasurables.lastIndex) {
                drawLine(
                    color = Grey75,
                    strokeWidth = 1.dp.toPx(),
                    start = Offset(
                        x = initialXAxisStartPadding + ((index + 0.75f) * spacePerDay),
                        y = size.height - xAxisBottomPadding
                    ),
                    end = Offset(
                        x = initialXAxisStartPadding + ((index + 0.75f) * spacePerDay),
                        y = yAxisTopPadding
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

                        val leftRatio = (1 - ((data.value - 0) / (40f - 0f))).coerceIn(0f, 40f)

                        val spacePerDay2 =
                            ((size.width - initialXAxisStartPadding - xAxisEndPadding) / sortedDescendingXAxis.size.minus(
                                1
                            ))
                        val x1 = data.getDay() * spacePerDay2 + 6.dp.toPx()

                        // 15.dp and 8.dp in the code below, is random number after some testing, to keep the horizontal line of each point exacly match the y axis
                        val y1 =
                            (height - xAxisBottomPadding - 15.dp.toPx()) * leftRatio + 8.dp.toPx()

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
        }
    }
}


@Preview
@Composable
private fun SensorDataGraphPreview() {
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
            List(7) { index ->
                val randomAdditionalValue = Random.nextInt(0, 30)
                SensorItemData.Temperature(
                    value = 10.0f + randomAdditionalValue,
                    timeString = getTimeString(
                        additionalTimesInMillis = 3600_000L * 24 * (index)
                    )
                )
            }.toImmutableList().apply {
                this.forEach {
                    println(it)
                }
            }
        }
        Column(
            Modifier
                .fillMaxSize()
                .background(color = Grey69)
                .padding(16.dp)
        ) {
            SensorDataGraph(
                sortedDescendingXAxis = sortedDescendingXAxis,
                sortedDescendingYAxis = sortedDescendingYAxis,
                sensorItemData = temperatureSensorItemDatas
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
                        val x1 = data.getDay() * spacePerDay2 + 6.dp.toPx()
                        //println("day.getday : ${data.getDay()}")
                        val y1 = (height - 32.dp.toPx() - 15.dp.toPx()) * leftRatio + 8.dp.toPx()
                        val x2 = nextData.getDay() * spacePerDay2 + 6.dp.toPx()
                        val y2 = (height - 32.dp.toPx() - 15.dp.toPx()) * rightRatio + 8.dp.toPx()
//                        println(
//                            "x1 : $x1, y1 : $y1, x2 : $x2, y2 : $y2"
//                        )

                        if (i == 0) {
                            println(
                                "move x1 : $x1, y1 : $y1\n day : ${data.getDay()}"
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
                val x = it.getDay() * spacePerDay2 + 6.dp.toPx()

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
                    timeString = getTimeString(
                        additionalTimesInMillis = 3600_000L * 24 * (0)
                    )
                ), SensorItemData.Temperature(
                    value = 15.0f,
                    timeString = getTimeString(
                        additionalTimesInMillis = 3600_000L * 24 * (1)
                    )
                ), SensorItemData.Temperature(
                    value = 20.0f,
                    timeString = getTimeString(
                        additionalTimesInMillis = 3600_000L * 24 * (2)
                    )
                ), SensorItemData.Temperature(
                    value = 25.0f,
                    timeString = getTimeString(
                        additionalTimesInMillis = 3600_000L * 24 * (3)
                    )
                ), SensorItemData.Temperature(
                    value = 30.0f,
                    timeString = getTimeString(
                        additionalTimesInMillis = 3600_000L * 24 * (4)
                    )
                ), SensorItemData.Temperature(
                    value = 35.0f,
                    timeString = getTimeString(
                        additionalTimesInMillis = 3600_000L * 24 * (5)
                    )
                ), SensorItemData.Temperature(
                    value = 40.0f,
                    timeString = getTimeString(
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