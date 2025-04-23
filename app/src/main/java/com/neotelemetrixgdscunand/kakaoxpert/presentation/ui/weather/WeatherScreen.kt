package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.weather

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.neotelemetrixgdscunand.kakaoxpert.R
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Black10
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Green55
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Green58
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Green60
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Grey90
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.KakaoXpertTheme
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.util.ImagePainterStable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreen(
    modifier: Modifier = Modifier,
    navigateUp: () -> Unit = {}
) {


    val scrollBehaviour = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        containerColor = Grey90,
        modifier = modifier
            .nestedScroll(scrollBehaviour.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = { },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Grey90,
                    scrolledContainerColor = Grey90
                ),
                scrollBehavior = scrollBehaviour,
                actions = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 24.dp, bottom = 12.dp),
                    ) {
                        IconButton(
                            modifier = Modifier
                                .align(Alignment.CenterStart),
                            onClick = navigateUp
                        ) {
                            Icon(
                                modifier = Modifier
                                    .width(21.dp)
                                    .height(20.dp),
                                imageVector = ImageVector
                                    .vectorResource(R.drawable.ic_arrow_left),
                                contentDescription = null,
                                tint = Black10
                            )
                        }

                        Row(
                            modifier = Modifier
                                .align(Alignment.Center),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                imageVector = ImageVector
                                    .vectorResource(R.drawable.ic_needle_location),
                                contentDescription = null
                            )

                            Spacer(Modifier.width(8.dp))

                            Text(
                                stringResource(R.string.padang),
                                style = MaterialTheme.typography.titleMedium
                            )

                            Spacer(Modifier.width(8.dp))

                            Icon(
                                imageVector = ImageVector
                                    .vectorResource(R.drawable.ic_down_arrow),
                                tint = Black10,
                                contentDescription = null
                            )
                        }

                    }
                }
            )
        }
    ) { innerPadding ->

        Column(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            WeatherScreenBody()
        }
    }
}

@Composable
fun WeatherScreenBody(
    modifier: Modifier = Modifier,
) {
    val contentItemModifier = remember {
        Modifier
            .padding(horizontal = 16.dp)
    }

    val configuration = LocalConfiguration.current
    val density = LocalDensity.current
    val cardModifier = remember {
        val xOffset = with(density) {
            (configuration.screenHeightDp.dp / 2.5f).toPx()
        }
        Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.linearGradient(
                    colorStops = arrayOf(
                        Pair(0f, Green55),
                        Pair(100f, Green58)
                    ),
                    start = Offset(x = xOffset, y = 0f),
                ),
                shape = RoundedCornerShape(16.dp),
            )
            .padding(vertical = 24.dp, horizontal = 16.dp)
    }

    val weatherData: ImmutableList<WeatherPredictionItemData> = remember {
        getDummyWeatherPredictionItemData().toPersistentList()
    }

    val weatherItemModifier = remember {
        Modifier
            .padding(start = 16.dp, end = 16.dp, bottom = 8.dp)
    }
    val scrollState = rememberScrollState()

    Spacer(Modifier.height(12.dp))

    Column(
        modifier = modifier
            .verticalScroll(scrollState)

    ) {
        Text(
            modifier = contentItemModifier,
            text = stringResource(R.string.hari_ini),
            style = MaterialTheme.typography.headlineSmall,
            color = Black10
        )

        Spacer(Modifier.height(16.dp))

        Column(
            modifier = contentItemModifier.then(cardModifier),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            ImagePainterStable(
                drawableResId = R.drawable.ic_weather,
                contentDescription = stringResource(R.string.gambar_cuaca)
            )

            Text(
                text = stringResource(R.string._17),
                style = MaterialTheme.typography.displayMedium,
                color = Color.White
            )
            Spacer(modifier = Modifier.width(24.dp))
            Text(
                stringResource(R.string.hujan_lebat),
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )
            Spacer(Modifier.height(8.dp))

            Row {
                Text(
                    stringResource(R.string.h_24),
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    stringResource(R.string.l_17),
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White
                )
            }

            HorizontalDivider(
                modifier =
                Modifier.padding(vertical = 16.dp),
                color = Green60
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
                        stringResource(R.string._87),
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
                        stringResource(R.string._2_km_jam),
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
                        stringResource(R.string._100mm),
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White
                    )
                }
            }
            Spacer(Modifier.height(27.dp))
        }

        Spacer(Modifier.height(16.dp))

        Text(
            modifier = contentItemModifier,
            text = stringResource(R.string._10_hari_yang_akan_datang),
            style = MaterialTheme.typography.headlineSmall,
            color = Black10
        )

        Spacer(Modifier.height(16.dp))

        weatherData.forEach {
            key(it.id) {
                WeatherPredictionItem(
                    modifier = weatherItemModifier,
                    date = it.date,
                    temperatureRange = it.temperatureRange,
                    windVelocity = it.windVelocity,
                    humidityPercentage = it.humidityPercentage
                )
            }
        }

        Spacer(Modifier.height(48.dp))
    }
}

@Preview(showBackground = true)
@Composable
private fun WeatherScreenPreview() {
    KakaoXpertTheme {
        WeatherScreen()
    }
}