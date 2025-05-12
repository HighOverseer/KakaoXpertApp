package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.weather.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.neotelemetrixgdscunand.kakaoxpert.R
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Green55
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Green60
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.KakaoXpertTheme
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Orange90
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.diagnosisresult.util.shimmeringEffect
import com.neotelemetrixgdscunand.kakaoxpert.presentation.dui.WeatherForecastItemDui
import com.neotelemetrixgdscunand.kakaoxpert.presentation.utils.ImagePainterStable
import com.neotelemetrixgdscunand.kakaoxpert.presentation.utils.UIText
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

@Composable
fun WeatherPredictionItem(
    modifier: Modifier = Modifier,
    weatherForecastItem: WeatherForecastItemDui
) {

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        onClick = {}
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Green55.copy(alpha = 0.75f), shape = RoundedCornerShape(8.dp))
                .padding(horizontal = 8.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = weatherForecastItem.date.getValue(),
                style = MaterialTheme.typography.labelMedium,
                color = Color.White
            )
            Spacer(Modifier.width(40.dp))
            Text(
                weatherForecastItem.maxTemperature.getValue() + "/" + weatherForecastItem.minTemperature.getValue(),
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )
            Spacer(Modifier.width(16.dp))
            ImagePainterStable(
                modifier = Modifier
                    .width(21.dp)
                    .height(18.dp),
                drawableResId = weatherForecastItem.iconResourceId,
                contentScale = ContentScale.Fit,
                contentDescription = null,
            )

            Spacer(Modifier.weight(1f))

            Image(
                imageVector = ImageVector.vectorResource(
                    R.drawable.ic_humidity
                ),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                colorFilter = ColorFilter.tint(Orange90),
                modifier = Modifier
                    .width(13.dp)
                    .height(8.dp)
            )
            Spacer(Modifier.width(4.dp))
            Text(
                text = weatherForecastItem.humidity.getValue(),
                style = MaterialTheme.typography.labelMedium,
                color = Color.White
            )
            Spacer(Modifier.width(24.dp))
            Image(
                imageVector = ImageVector.vectorResource(
                    R.drawable.ic_wind
                ),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                colorFilter = ColorFilter.tint(Orange90),
                modifier = Modifier
                    .width(12.6.dp)
                    .height(12.6.dp)
            )
            Spacer(Modifier.width(4.dp))
            Text(
                text = weatherForecastItem.windVelocity.getValue(),
                style = MaterialTheme.typography.labelMedium,
                color = Color.White
            )
        }
    }
}

@Composable
fun WeatherPredictionItemLoading(
    modifier: Modifier = Modifier,
) {

    val gradientShimmeringColor = remember {
        persistentListOf(
            Green60.copy(alpha = 0.45f),
            Green55.copy(alpha = 0.1f),
        ).toImmutableList()
    }

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        onClick = {}
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Green55.copy(alpha = 0.75f), shape = RoundedCornerShape(8.dp))
                .padding(horizontal = 8.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .width(32.dp)
                    .height(14.dp)
                    .shimmeringEffect(gradientShimmeringColor)
            )
            Spacer(Modifier.width(40.dp))
            Box(
                modifier = Modifier
                    .width(32.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .width(32.dp)
                    .height(14.dp)
                    .shimmeringEffect(gradientShimmeringColor)
            )
            Spacer(Modifier.width(16.dp))
            ImagePainterStable(
                modifier = Modifier
                    .width(21.dp)
                    .height(18.dp),
                drawableResId = R.drawable.ic_weather_cloudy,
                contentScale = ContentScale.Fit,
                contentDescription = null,
            )

            Spacer(Modifier.weight(1f))

            Image(
                imageVector = ImageVector.vectorResource(
                    R.drawable.ic_humidity
                ),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                colorFilter = ColorFilter.tint(Orange90),
                modifier = Modifier
                    .width(13.dp)
                    .height(8.dp)
            )
            Spacer(Modifier.width(4.dp))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .width(32.dp)
                    .height(14.dp)
                    .shimmeringEffect(gradientShimmeringColor)
            )
            Spacer(Modifier.width(24.dp))
            Image(
                imageVector = ImageVector.vectorResource(
                    R.drawable.ic_wind
                ),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                colorFilter = ColorFilter.tint(Orange90),
                modifier = Modifier
                    .width(12.6.dp)
                    .height(12.6.dp)
            )
            Spacer(Modifier.width(4.dp))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .width(32.dp)
                    .height(14.dp)
                    .shimmeringEffect(gradientShimmeringColor)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun WeatherPredictionItemPreview() {
    KakaoXpertTheme {
        WeatherPredictionItem(
            modifier = Modifier,
            WeatherForecastItemDui(
                date = UIText.DynamicString("Sen 21"),
                maxTemperature = UIText.DynamicString("24°"),
                minTemperature = UIText.DynamicString("17°"),
                iconResourceId = R.drawable.ic_weather_cloudy,
                humidity = UIText.DynamicString("70%"),
                windVelocity = UIText.DynamicString("10 km/h")
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun WeatherPredictionItemLoadingPreview() {
    KakaoXpertTheme {
        WeatherPredictionItemLoading(
            modifier = Modifier
        )
    }
}