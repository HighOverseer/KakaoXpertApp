package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.sensordatadetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.neotelemetrixgdscunand.kakaoxpert.R
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.SensorItemData
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Black10
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Grey90
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.KakaoXpertTheme
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Orange85
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.sensordatadetails.component.SensorDataGraph
import com.neotelemetrixgdscunand.kakaoxpert.presentation.utils.collectChannelWhenStarted
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.launch
import java.util.Calendar


@Composable
fun SensorDataDetailScreen(
    modifier: Modifier = Modifier,
    navigateUp: () -> Unit = {},
    viewModel: SensorDataDetailViewModel = hiltViewModel(),
    showSnackbar: (String) -> Unit = {}
) {

    val lifecycle = LocalLifecycleOwner.current
    val context = LocalContext.current
    LaunchedEffect(true) {
        lifecycle.collectChannelWhenStarted(viewModel.uiEvent) {
            when (it) {
                is SensorDataDetailUIEvent.OnFailedGettingSensorData -> {
                    showSnackbar(it.errorUIText.getValue(context))
                }
            }
        }
    }

    val temperatureSensorData by viewModel.temperatureSensorData.collectAsStateWithLifecycle()
    val humiditySensorData by viewModel.humiditySensorData.collectAsStateWithLifecycle()
    val lightIntensitySensorData by viewModel.lightIntensitySensorData.collectAsStateWithLifecycle()

    SensorDataDetailContent(
        modifier = modifier,
        navigateUp = navigateUp,
        temperatureSensorData = temperatureSensorData,
        humiditySensorData = humiditySensorData,
        lightIntensitySensorData = lightIntensitySensorData
    )

}

@Composable
fun SensorDataDetailContent(
    modifier: Modifier = Modifier,
    navigateUp: () -> Unit = {},
    temperatureSensorData: ImmutableList<SensorItemData>,
    humiditySensorData: ImmutableList<SensorItemData>,
    lightIntensitySensorData: ImmutableList<SensorItemData>
) {

//    val temperatureSensorItemDatas = remember {
//        List(14) { index ->
//            val randomAdditionalValue = Random.nextInt(0, 30)
//            SensorItemData.Temperature(
//                value = 10.0f + randomAdditionalValue,
//                timeInMillis = getTimeInMillis(
//                    additionalTimesInMillis = (3600_000L * 24f * (index / 2f)).toLong()
//                )
//            )
//        }.toImmutableList().apply {
//            this.forEach {
//                println(it)
//            }
//        }
//    }
//
//    val humiditySensorItemDatas = remember {
//        List(14) { index ->
//            val randomAdditionalValue = Random.nextInt(0, 30)
//            SensorItemData.Humidity(
//                value = 10.0f + randomAdditionalValue,
//                timeInMillis = getTimeInMillis(
//                    additionalTimesInMillis = (3600_000L * 24f * (index / 2f)).toLong()
//                )
//            )
//        }.toImmutableList().apply {
//            this.forEach {
//                println(it)
//            }
//        }
//    }
//
//    val lightIntensitySensorItemDatas = remember {
//        List(14) { index ->
//            val randomAdditionalValue = Random.nextInt(0, 30)
//            SensorItemData.LightIntensity(
//                value = 10.0f + randomAdditionalValue,
//                timeInMillis = getTimeInMillis(
//                    additionalTimesInMillis = (3600_000L * 24f * (index / 2f)).toLong()
//                )
//            )
//        }.toImmutableList().apply {
//            this.forEach {
//                println(it)
//            }
//        }
//    }


    val scrollState = rememberScrollState()
    var isScrollEnabled by remember { mutableStateOf(true) }
    val onSlidingGraphPointer = remember {
        {
            isScrollEnabled = false
        }
    }

    val onFinishSlidingGraphPointer = remember {
        {
            isScrollEnabled = true
        }
    }

    val coroutineScope = rememberCoroutineScope()
    val onDelegateScroll: (Float) -> Unit = remember {
        {
            coroutineScope.launch {
                scrollState.scroll {
                    scrollBy(it)
                }
            }
        }
    }

    val baseDayOfTheYear = remember {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -6)
        calendar.get(Calendar.DAY_OF_YEAR)
    }

    var isNavigatingUp by remember {
        mutableStateOf(false)
    }


    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Grey90)
            .verticalScroll(scrollState, isScrollEnabled)

    ) {
        Spacer(Modifier.height(24.dp))

        Box(
            Modifier
                .fillMaxWidth()
        ) {
            IconButton(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .align(Alignment.CenterStart),
                onClick = {
                    isNavigatingUp = true
                    navigateUp()
                }
            ) {
                Icon(
                    modifier = Modifier
                        .size(20.dp),
                    imageVector = ImageVector
                        .vectorResource(R.drawable.ic_arrow_left),
                    tint = Black10,
                    contentDescription = stringResource(R.string.kembali)
                )
            }
            Text(
                modifier = Modifier
                    .align(Alignment.Center),
                text = stringResource(R.string.overview),
                style = MaterialTheme.typography.headlineSmall,
                color = Black10
            )
        }

        Spacer(Modifier.height(24.dp))

        Column(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier =
                    Modifier
                        .height(18.dp)
                        .aspectRatio(0.565f),
                    imageVector = ImageVector
                        .vectorResource(R.drawable.ic_thermometer),
                    contentDescription = null,
                    tint = Orange85
                )

                Spacer(Modifier.width(8.dp))

                Text(
                    text = stringResource(R.string.suhu),
                    style = MaterialTheme.typography.headlineSmall,
                    color = Black10
                )
            }

            Spacer(Modifier.height(16.dp))

            SensorDataGraph(
                sensorItemData = temperatureSensorData,
                onProcessSlidingGraphPointer = onSlidingGraphPointer,
                onFinishSlidingGraphPointer = onFinishSlidingGraphPointer,
                onDelegateScroll = onDelegateScroll,
                baseDayOfTheYear = baseDayOfTheYear,
                isNavigatingUp = isNavigatingUp
            )

            Spacer(Modifier.height(32.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier =
                    Modifier
                        .height(16.dp)
                        .aspectRatio(0.714f),
                    imageVector = ImageVector
                        .vectorResource(R.drawable.ic_drop),
                    contentDescription = null,
                    tint = Orange85
                )

                Spacer(Modifier.width(8.dp))

                Text(
                    text = stringResource(R.string.kelembapan),
                    style = MaterialTheme.typography.headlineSmall,
                    color = Black10
                )
            }

            Spacer(Modifier.height(16.dp))

            SensorDataGraph(
                sensorItemData = humiditySensorData,
                onProcessSlidingGraphPointer = onSlidingGraphPointer,
                onFinishSlidingGraphPointer = onFinishSlidingGraphPointer,
                onDelegateScroll = onDelegateScroll,
                baseDayOfTheYear = baseDayOfTheYear,
                isNavigatingUp = isNavigatingUp
            )

            Spacer(Modifier.height(32.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier =
                    Modifier
                        .height(20.dp)
                        .aspectRatio(1f),
                    imageVector = ImageVector
                        .vectorResource(R.drawable.ic_sun),
                    contentDescription = null,
                    tint = Orange85
                )

                Spacer(Modifier.width(8.dp))

                Text(
                    text = stringResource(R.string.intensitas_cahaya),
                    style = MaterialTheme.typography.headlineSmall,
                    color = Black10
                )
            }

            Spacer(Modifier.height(16.dp))

            SensorDataGraph(
                sensorItemData = lightIntensitySensorData,
                onProcessSlidingGraphPointer = onSlidingGraphPointer,
                onFinishSlidingGraphPointer = onFinishSlidingGraphPointer,
                onDelegateScroll = onDelegateScroll,
                baseDayOfTheYear = baseDayOfTheYear,
                isNavigatingUp = isNavigatingUp
            )

        }

        Spacer(Modifier.height(81.dp))

    }
}

@Preview
@Composable
private fun SensorDataDetailScreenPreview() {
    KakaoXpertTheme {
        SensorDataDetailScreen()
    }
}