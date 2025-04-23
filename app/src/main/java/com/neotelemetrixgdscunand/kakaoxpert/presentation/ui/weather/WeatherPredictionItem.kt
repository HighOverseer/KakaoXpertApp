package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.weather

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.neotelemetrixgdscunand.kakaoxpert.R
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Black10
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.KakaoXpertTheme

@Composable
fun WeatherPredictionItem(
    modifier: Modifier = Modifier,
    date: String,
    temperatureRange: String,
    humidityPercentage: Int,
    windVelocity: Int
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(color = Color.White, shape = RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = date,
            style = MaterialTheme.typography.labelMedium,
            color = Black10
        )
        Spacer(Modifier.width(40.dp))
        Text(
            temperatureRange,
            style = MaterialTheme.typography.titleMedium,
            color = Black10
        )
        Spacer(Modifier.width(16.dp))
        Image(
            modifier = Modifier
                .width(21.dp)
                .height(18.dp),
            imageVector = ImageVector
                .vectorResource(R.drawable.ic_rain),
            contentScale = ContentScale.Fit,
            contentDescription = null
        )

        Spacer(Modifier.weight(1f))

        Image(
            imageVector = ImageVector.vectorResource(
                R.drawable.ic_humidity
            ),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .width(8.dp)
                .height(13.dp)
        )
        Spacer(Modifier.width(4.dp))
        Text(
            text = stringResource(R.string.humidity_data, humidityPercentage.toString()),
            style = MaterialTheme.typography.labelMedium,
            color = Black10
        )
        Spacer(Modifier.width(24.dp))
        Image(
            imageVector = ImageVector.vectorResource(
                R.drawable.ic_wind
            ),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .width(12.6.dp)
                .height(12.6.dp)
        )
        Spacer(Modifier.width(4.dp))
        Text(
            text = stringResource(R.string.wind_velocity_data, windVelocity),
            style = MaterialTheme.typography.labelMedium,
            color = Black10
        )
    }
}

@Preview
@Composable
private fun WeatherPredictionItemPreview() {
    KakaoXpertTheme {
        WeatherPredictionItem(
            modifier = Modifier,
            "Sen 21",
            "31°/24°",
            30,
            2
        )
    }
}