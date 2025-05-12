package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.toplevel.home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.neotelemetrixgdscunand.kakaoxpert.R
import com.neotelemetrixgdscunand.kakaoxpert.presentation.dui.WeatherForecastOverviewDui
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Green55
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Green58
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Green60
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.KakaoXpertTheme
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Orange85
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Pink
import com.neotelemetrixgdscunand.kakaoxpert.presentation.utils.AsyncImagePainterStable
import com.neotelemetrixgdscunand.kakaoxpert.presentation.utils.ImagePainterStable

@Composable
fun HomeHeaderSection(
    modifier: Modifier = Modifier,
    navigateToNotification: () -> Unit = {},
    weatherForecastOverview: WeatherForecastOverviewDui? = null,
    currentLocationProvider: () -> String? = { null },
    navigateToWeather: () -> Unit = {},
) {

    var inflatedCardHeight by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current
    val parentModifier = remember {
        modifier
            .background(
                brush = Brush.linearGradient(
                    Pair(1f, Green58),
                    Pair(1f, Green55)
                ),
                shape = RoundedCornerShape(
                    bottomStart = 16.dp,
                    bottomEnd = 16.dp
                )
            )
            .onGloballyPositioned {
                inflatedCardHeight = with(density) {
                    it.size.height.toDp()
                }
            }
    }

    Box(
        modifier = parentModifier
    ) {

        val backgroundIconModifier = remember(inflatedCardHeight) {
            val yOffset = with(density) {
                (-inflatedCardHeight / 2.5f).roundToPx()
            }
            val xOffset = with(density) {
                (-inflatedCardHeight / 2f).roundToPx()
            }
            Modifier
                .size(inflatedCardHeight)
                .heightIn(max = inflatedCardHeight)
                .offset {
                    IntOffset(xOffset, yOffset)
                }
                .align(Alignment.TopStart)
        }

        ImagePainterStable(
            modifier = backgroundIconModifier,
            drawableResId = R.drawable.header_bg,
            contentScale = ContentScale.FillBounds,
            contentDescription = null,
        )

        val backgroundIcon2Modifier = remember(inflatedCardHeight) {
            val yOffset = with(density) {
                (-inflatedCardHeight / 2.35f).roundToPx()
            }
            val xOffset = with(density) {
                (inflatedCardHeight / 3.5f).roundToPx()
            }
            Modifier
                .size(inflatedCardHeight * 0.8f)
                .aspectRatio(0.76f)
                .heightIn(max = inflatedCardHeight)
                .offset {
                    IntOffset(xOffset, yOffset)
                }
                .align(Alignment.CenterEnd)
        }

        ImagePainterStable(
            modifier = backgroundIcon2Modifier,
            drawableResId = R.drawable.header2_bg,
            contentScale = ContentScale.FillBounds,
            contentDescription = null,
        )


        val columnModifier = remember {
            Modifier.padding(horizontal = 16.dp, vertical = 25.dp)
        }
        Column(
            modifier = columnModifier
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                val circleImageModifier = remember {
                    Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                }

                Box(
                    modifier = circleImageModifier
                ) {
                    AsyncImagePainterStable(
                        modifier = Modifier
                            .align(Alignment.Center),
                        alignment = Alignment.Center,
                        imageDrawableResId = R.drawable.dummy_profile,
                        placeholderResId = R.drawable.ic_camera,
                        contentDescription = stringResource(R.string.profile_photo),
                        contentScale = ContentScale.Crop
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        imageVector = ImageVector
                            .vectorResource(R.drawable.ic_needle_location),
                        contentDescription = null
                    )
                    Text(
                        text = currentLocationProvider()
                            ?: stringResource(R.string.tidak_diketahui),
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White
                    )
                    Image(
                        imageVector = ImageVector
                            .vectorResource(
                                R.drawable.ic_down_arrow
                            ),
                        contentDescription = null,
                    )
                }

                val iconBellModifier = remember {
                    Modifier
                        .border(
                            width = 1.dp,
                            color = Pink,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(9.dp)
                }
                Box(
                    modifier = iconBellModifier
                        .clickable(onClick = navigateToNotification)
                ) {
                    Image(
                        imageVector = ImageVector
                            .vectorResource(R.drawable.ic_bell),
                        contentDescription = stringResource(R.string.notification)
                    )
                }

            }

            val autoSpacerModifier = remember {
                val cardToHeadlineInfoMarginRatio = 0.0292f
                Modifier
                    .fillMaxHeight(cardToHeadlineInfoMarginRatio)
                    .defaultMinSize(minHeight = 16.dp)
            }
            Spacer(
                modifier = autoSpacerModifier
            )

            val cardModifier = remember {
                Modifier
                    .fillMaxWidth()
                    .background(
                        color = Green60,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .clip(RoundedCornerShape(16.dp))
                    .padding(top = 18.dp, bottom = 0.dp)
            }
            Column(
                modifier = cardModifier
            ) {
                Column(
                    Modifier
                        .padding(start = 16.dp, end = 16.dp, bottom = 10.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = weatherForecastOverview?.currentTemperature?.getValue() ?: "-°",
                            style = MaterialTheme.typography.displayMedium,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.width(24.dp))
                        Column {
                            Text(
                                weatherForecastOverview?.name?.getValue() ?: "",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.White
                            )
                            Spacer(Modifier.height(8.dp))
                            Text(
                                stringResource(
                                    R.string.h_24,
                                    weatherForecastOverview?.maxTemperature?.getValue() ?: "-"
                                ),
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.White
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                stringResource(
                                    R.string.l_17,
                                    weatherForecastOverview?.minTemperature?.getValue() ?: "-"
                                ),
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.White
                            )
                        }

                        Spacer(Modifier.weight(1f))
                        AsyncImagePainterStable(
                            imageDrawableResId = weatherForecastOverview?.iconResourceId
                                ?: R.drawable.ic_weather_cloudy,
                            contentScale = ContentScale.Fit,
                            contentDescription = stringResource(R.string.gambar_cuaca)
                        )
                    }

                    Divider(
                        modifier =
                        Modifier
                            .padding(vertical = 16.dp),
                        color = Green55
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

                val bottomCardModifier = remember {
                    Modifier
                        .fillMaxWidth()
                        .padding(0.dp)
                        .height(32.dp)
                        .wrapContentHeight()
                }
                Card(
                    modifier = bottomCardModifier,
                    shape = RoundedCornerShape(bottomEnd = 16.dp, bottomStart = 16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Orange85
                    ),
                    onClick = navigateToWeather,
                ) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.lihat_selengkapnya),
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.W300
                            ),
                            color = Color.White
                        )

                        Spacer(Modifier.weight(1f))

                        Icon(
                            modifier = Modifier
                                .height(14.dp)
                                .aspectRatio(0.57f),
                            imageVector = ImageVector
                                .vectorResource(R.drawable.ic_right_arrow),
                            contentDescription = null,
                            tint = Color.White
                        )

                    }
                }
            }

        }

    }

}

@Preview
@Composable
private fun HomeHeaderSectionPreview() {
    KakaoXpertTheme {
        HomeHeaderSection(
            modifier = Modifier.fillMaxWidth()
        )
    }

}