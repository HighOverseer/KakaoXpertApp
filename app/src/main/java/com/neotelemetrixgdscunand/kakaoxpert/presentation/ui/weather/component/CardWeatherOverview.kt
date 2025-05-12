package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.weather.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.neotelemetrixgdscunand.kakaoxpert.R
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Green55
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Green58
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Green60
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.KakaoXpertTheme
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Pink
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.diagnosisresult.util.shimmeringEffect
import com.neotelemetrixgdscunand.kakaoxpert.presentation.dui.WeatherForecastOverviewDui
import com.neotelemetrixgdscunand.kakaoxpert.presentation.utils.ImagePainterStable
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

@Composable
fun CardWeatherOverview(
    modifier: Modifier = Modifier,
    weatherForecastOverview: WeatherForecastOverviewDui? = null,
    isLoadingProvider: () -> Boolean = { false }
) {
    if (isLoadingProvider()) {
        CardWeatherOverviewLoading(
            modifier
        )
    } else {
        val columnModifier = remember {
            Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.linearGradient(
                        colorStops = arrayOf(
                            Pair(0f, Green55.copy(alpha = 0.9f)),
                            Pair(100f, Green58.copy(alpha = 0.9f))
                        )
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(vertical = 24.dp, horizontal = 16.dp)
        }

        Card(
            modifier = modifier,
            shape = RoundedCornerShape(16.dp),
            onClick = {}
        ) {
            Column(
                modifier = columnModifier,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                ImagePainterStable(
                    drawableResId = weatherForecastOverview?.iconResourceId
                        ?: R.drawable.ic_weather_cloudy,
                    contentDescription = stringResource(R.string.gambar_cuaca)
                )

                Text(
                    text = weatherForecastOverview?.currentTemperature?.getValue() ?: "-°",
                    style = MaterialTheme.typography.displayMedium,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    weatherForecastOverview?.name?.getValue() ?: "",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White
                )
                Spacer(Modifier.height(8.dp))

                Row {
                    Text(
                        stringResource(
                            R.string.h_24,
                            weatherForecastOverview?.maxTemperature?.getValue() ?: "-"
                        ),
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        stringResource(
                            R.string.l_17,
                            weatherForecastOverview?.minTemperature?.getValue() ?: "-"
                        ),
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White
                    )
                }

                HorizontalDivider(
                    modifier =
                    Modifier.padding(vertical = 16.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            stringResource(R.string.kelembapan),
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.W300
                            ),
                            color = Color.White
                        )
                        Spacer(Modifier.height(7.dp))
                        Text(
                            weatherForecastOverview?.humidity?.getValue() ?: "-",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White
                        )
                    }
                    Column {
                        Text(
                            stringResource(R.string.kec_angin),
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.W300
                            ),
                            color = Color.White
                        )
                        Spacer(Modifier.height(7.dp))
                        Text(
                            weatherForecastOverview?.windVelocity?.getValue() ?: "-",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White
                        )
                    }
                    Column {
                        Text(
                            stringResource(R.string.curah_hujan),
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.W300
                            ),
                            color = Color.White
                        )
                        Spacer(Modifier.height(7.dp))
                        Text(
                            weatherForecastOverview?.rainfall?.getValue() ?: "-",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }

}

@Composable
private fun CardWeatherOverviewLoading(modifier: Modifier = Modifier) {
    val columnModifier = remember {
        Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.linearGradient(
                    colorStops = arrayOf(
                        Pair(0f, Green55.copy(alpha = 0.9f)),
                        Pair(100f, Green58.copy(alpha = 0.9f))
                    )
                ),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(vertical = 24.dp, horizontal = 16.dp)
    }

    val gradientShimmeringColor = remember {
        persistentListOf(
            Green60,
            Green55.copy(alpha = 0.3f),
        ).toImmutableList()
    }

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        onClick = {}
    ) {


        Column(
            modifier = columnModifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            ImagePainterStable(
                drawableResId = R.drawable.ic_weather_cloudy,
                contentDescription = stringResource(R.string.gambar_cuaca)
            )


            Spacer(modifier = Modifier.height(24.dp))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .width(32.dp)
                    .height(17.dp)
                    .shimmeringEffect(gradientShimmeringColor)
            )
            Spacer(Modifier.height(8.dp))

            Row {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .width(32.dp)
                        .height(17.dp)
                        .shimmeringEffect(gradientShimmeringColor)
                )
                Spacer(Modifier.width(4.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .width(32.dp)
                        .height(17.dp)
                        .shimmeringEffect(gradientShimmeringColor)
                )
            }

            HorizontalDivider(
                modifier =
                Modifier.padding(vertical = 16.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        stringResource(R.string.kelembapan),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.W300
                        ),
                        color = Pink
                    )
                    Spacer(Modifier.height(7.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .width(32.dp)
                            .height(17.dp)
                            .shimmeringEffect(
                                gradientShimmeringColor = gradientShimmeringColor
                            )
                    )
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        stringResource(R.string.kec_angin),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.W300
                        ),
                        color = Pink
                    )
                    Spacer(Modifier.height(7.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .width(32.dp)
                            .height(17.dp)
                            .shimmeringEffect(gradientShimmeringColor)
                    )
                }
                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        stringResource(R.string.curah_hujan),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.W300
                        ),
                        color = Pink
                    )
                    Spacer(Modifier.height(7.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .width(32.dp)
                            .height(17.dp)
                            .shimmeringEffect(gradientShimmeringColor)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun CardWeatherOverviewLoadingPreview() {
    KakaoXpertTheme {
        CardWeatherOverviewLoading()
    }
}