package com.neotelemetrixgdscunand.kamekapp.presentation.ui.weather

import android.content.Context
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.common.api.ResolvableApiException
import com.neotelemetrixgdscunand.kakaoxpert.R
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Black10
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Grey90
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.KakaoXpertTheme
import com.neotelemetrixgdscunand.kamekapp.presentation.dui.WeatherForecastItemDui
import com.neotelemetrixgdscunand.kamekapp.presentation.dui.WeatherForecastOverviewDui
import com.neotelemetrixgdscunand.kamekapp.presentation.ui.weather.component.CardWeatherOverview
import com.neotelemetrixgdscunand.kamekapp.presentation.ui.weather.component.WeatherPredictionItem
import com.neotelemetrixgdscunand.kamekapp.presentation.ui.weather.component.WeatherPredictionItemLoading
import com.neotelemetrixgdscunand.kamekapp.presentation.utils.collectChannelWhenStarted
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun WeatherScreen(
    modifier: Modifier = Modifier,
    navigateUp: () -> Unit = {},
    showSnackbar: (String) -> Unit = {},
    viewModel: WeatherViewModel = hiltViewModel(),
    isLocationPermissionGrantedProvider: () -> Boolean? = { false },
    checkLocationPermission: (Context, ManagedActivityResultLauncher<Array<String>, Map<String, Boolean>>) -> Unit = { _, _ -> },
    rememberLocationPermissionRequest: @Composable () -> ManagedActivityResultLauncher<Array<String>, Map<String, Boolean>> = {
        rememberLauncherForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { }
    },
    rememberLocationSettingResolutionLauncher: @Composable () -> ManagedActivityResultLauncher<IntentSenderRequest, ActivityResult> = {
        rememberLauncherForActivityResult(
            ActivityResultContracts.StartIntentSenderForResult()
        ) {}
    },
) {
    val lifecycle = LocalLifecycleOwner.current
    val context = LocalContext.current

    val locationPermissionRequest = rememberLocationPermissionRequest()
    val isLocationPermissionGranted = isLocationPermissionGrantedProvider()
    val locationPermissionDeniedMessage = stringResource(R.string.fitur_prediksi_cuaca_tidak_bisa)

    LaunchedEffect(isLocationPermissionGranted) {
        if (isLocationPermissionGranted == null) {
            checkLocationPermission(
                context,
                locationPermissionRequest
            )
        } else if (isLocationPermissionGranted == true) {
            viewModel.startLocationUpdates()
        } else {
            showSnackbar(locationPermissionDeniedMessage)
            viewModel.stopLocationUpdates()
        }
    }

    val locationSettingResolutionLauncher = rememberLocationSettingResolutionLauncher()
    val locationSettingResolvableErrorMessage =
        stringResource(R.string.maaf_sepertinya_anda_perlu_mengaktifkan_beberapa_pengaturan_lokasi)
    LaunchedEffect(true) {
        lifecycle.collectChannelWhenStarted(viewModel.uiEvent) {
            when (it) {
                is WeatherUIEvent.OnFailedFetchWeatherForecast -> {
                    showSnackbar(it.errorUIText.getValue(context))
                }

                is WeatherUIEvent.OnLocationResolvableError -> {
                    showSnackbar(locationSettingResolvableErrorMessage)

                    if (it.exception is ResolvableApiException) {
                        locationSettingResolutionLauncher.launch(
                            IntentSenderRequest.Builder(it.exception.resolution).build()
                        )
                    }
                }

                is WeatherUIEvent.OnLocationUnknownError -> {
                    showSnackbar(it.errorUIText.getValue(context))
                }
            }

        }
    }

    LaunchedEffect(true) {
        lifecycle.collectChannelWhenStarted(viewModel.onMessageEvent) {
            showSnackbar(it.getValue(context))
        }
    }

    val weatherForecastOverviewDui by viewModel.weatherForecastOverview.collectAsStateWithLifecycle()
    val weatherForecastDataForSeveralDays by viewModel.weatherForecastForSeveralDays.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val location by viewModel.currentLocation.collectAsStateWithLifecycle()

    WeatherContent(
        modifier = modifier,
        navigateUp = navigateUp,
        weatherForecastOverview = weatherForecastOverviewDui,
        weatherForecastDataForSeveralDays = weatherForecastDataForSeveralDays,
        isLoadingProvider = { isLoading },
        currentLocationNameProvider = { location?.name }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherContent(
    modifier: Modifier = Modifier,
    navigateUp: () -> Unit = {},
    weatherForecastOverview: WeatherForecastOverviewDui? = null,
    weatherForecastDataForSeveralDays: ImmutableList<WeatherForecastItemDui> = persistentListOf(),
    isLoadingProvider: () -> Boolean = { false },
    currentLocationNameProvider: () -> String? = { null }
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
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 24.dp, bottom = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
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
                                .weight(1f),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Image(
                                imageVector = ImageVector
                                    .vectorResource(R.drawable.ic_needle_location),
                                contentDescription = null
                            )

                            Spacer(Modifier.width(8.dp))

                            Text(
                                currentLocationNameProvider()
                                    ?: stringResource(R.string.tidak_diketahui),
                                style = MaterialTheme.typography.titleMedium
                            )
                        }

                        Spacer(Modifier.size(48.dp))

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
            WeatherScreenBody(
                weatherForecastOverview = weatherForecastOverview,
                weatherForecastDataForSeveralDays = weatherForecastDataForSeveralDays,
                isLoadingProvider = isLoadingProvider
            )
        }
    }
}

@Composable
fun WeatherScreenBody(
    modifier: Modifier = Modifier,
    weatherForecastOverview: WeatherForecastOverviewDui? = null,
    weatherForecastDataForSeveralDays: ImmutableList<WeatherForecastItemDui> = persistentListOf(),
    isLoadingProvider: () -> Boolean = { false },
) {
    val contentItemModifier = remember {
        Modifier
            .padding(horizontal = 16.dp)
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

        CardWeatherOverview(
            modifier = contentItemModifier,
            weatherForecastOverview = weatherForecastOverview,
            isLoadingProvider = isLoadingProvider
        )

        Spacer(Modifier.height(16.dp))

        Text(
            modifier = contentItemModifier,
            text = stringResource(R.string._10_hari_yang_akan_datang),
            style = MaterialTheme.typography.headlineSmall,
            color = Black10
        )

        Spacer(Modifier.height(16.dp))

        if (isLoadingProvider()) {
            repeat(6) {
                key(it) {
                    WeatherPredictionItemLoading(
                        modifier = weatherItemModifier
                    )
                }
            }
        } else {
            weatherForecastDataForSeveralDays.forEach {
                key(it.hashCode()) {
                    WeatherPredictionItem(
                        modifier = weatherItemModifier,
                        weatherForecastItem = it
                    )
                }
            }
        }


        Spacer(Modifier.height(48.dp))
    }
}


@Preview(showBackground = true)
@Composable
private fun WeatherScreenPreview() {
    KakaoXpertTheme {
        WeatherContent()
    }
}