package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.toplevel.home

import android.content.Context
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.common.api.ResolvableApiException
import com.neotelemetrixgdscunand.kakaoxpert.R
import com.neotelemetrixgdscunand.kakaoxpert.presentation.dui.AnalysisSessionPreviewDui
import com.neotelemetrixgdscunand.kakaoxpert.presentation.dui.CocoaAverageSellPriceInfoDui
import com.neotelemetrixgdscunand.kakaoxpert.presentation.dui.IoTDataOverviewDui
import com.neotelemetrixgdscunand.kakaoxpert.presentation.dui.NewsItemDui
import com.neotelemetrixgdscunand.kakaoxpert.presentation.dui.WeatherForecastOverviewDui
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.KakaoXpertTheme
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.toplevel.home.component.HomeDiagnosisHistory
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.toplevel.home.component.HomeHeaderSection
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.toplevel.home.component.IoTDataOverviewSection
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.toplevel.home.component.IoTDevicesConnectivityStatusSection
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.toplevel.home.component.PriceInfoSection
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.toplevel.home.component.SectionHeadline
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.toplevel.home.component.WeeklyNewsPreviewSection
import com.neotelemetrixgdscunand.kakaoxpert.presentation.utils.collectChannelWhenStarted
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
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
    navigateToNews: () -> Unit = {},
    navigateToShop: () -> Unit = {},
    navigateToWeather: () -> Unit = {},
    navigateToNewsDetail: (Int) -> Unit = {},
    navigateToDiagnosisResult: (Int) -> Unit = { _ -> },
    navigateToNotification: () -> Unit = {},
    navigateToAnalysisHistoryMenu: () -> Unit = {},
    showSnackbar: (String) -> Unit = {}
) {

    val lifecycle = LocalLifecycleOwner.current
    val context = LocalContext.current

    val locationPermissionRequest = rememberLocationPermissionRequest()
    val isLocationPermissionGranted = isLocationPermissionGrantedProvider()
    val locationPermissionDeniedMessage = stringResource(R.string.fitur_prediksi_cuaca_tidak_bisa)

    LaunchedEffect(isLocationPermissionGranted) {
        when (isLocationPermissionGranted) {
            null -> {
                checkLocationPermission(
                    context,
                    locationPermissionRequest
                )
            }

            true -> {
                viewModel.startLocationUpdates()
            }

            else -> {
                showSnackbar(locationPermissionDeniedMessage)
                viewModel.stopLocationUpdates()
            }
        }
    }

    val locationSettingResolutionLauncher = rememberLocationSettingResolutionLauncher()
    val locationSettingResolvableErrorMessage =
        stringResource(R.string.maaf_sepertinya_anda_perlu_mengaktifkan_beberapa_pengaturan_lokasi)
    LaunchedEffect(true) {
        lifecycle.collectChannelWhenStarted(viewModel.uiEvent) {
            when (it) {
                is HomeUIEvent.OnFailedFetchWeatherForecast -> {
                    showSnackbar(it.errorUIText.getValue(context))
                }

                is HomeUIEvent.OnLocationResolvableError -> {
                    showSnackbar(locationSettingResolvableErrorMessage)

                    if (it.exception is ResolvableApiException) {
                        locationSettingResolutionLauncher.launch(
                            IntentSenderRequest.Builder(it.exception.resolution).build()
                        )
                    }
                }

                is HomeUIEvent.OnLocationUnknownError -> {
                    showSnackbar(it.errorUIText.getValue(context))
                }

                is HomeUIEvent.OnFailedFetchNewsItems -> {
                    showSnackbar(it.errorUIText.getValue(context))
                }

                is HomeUIEvent.OnFailedGetIoTDataOverview -> {
                    showSnackbar(it.errorUIText.getValue(context))
                }
            }
        }
    }


    val diagnosisSessionPreviews by viewModel.diagnosisHistory.collectAsStateWithLifecycle()
    val weatherForecastOverview by viewModel.weatherForecastOverview.collectAsStateWithLifecycle()
    val currentLocation by viewModel.currentLocation.collectAsStateWithLifecycle()
    val newsItems by viewModel.newsItems.collectAsStateWithLifecycle()
    val isLoadingNewsItemsPreview by viewModel.isLoadingNewsItemsPreview.collectAsStateWithLifecycle()
    val ioTDataOverviewDui by viewModel.iotDataOverview.collectAsStateWithLifecycle()
    val cocoaAverageSellPriceInfo by viewModel.cocoaPriceInfo.collectAsStateWithLifecycle()


    HomeContent(
        modifier = modifier,
        navigateToNews = navigateToNews,
        navigateToShop = navigateToShop,
        navigateToWeather = navigateToWeather,
        navigateToNewsDetail = navigateToNewsDetail,
        analysisSessionPreviews = diagnosisSessionPreviews,
        navigateToDiagnosisResult = navigateToDiagnosisResult,
        navigateToNotification = navigateToNotification,
        navigateToAnalysisHistoryMenu = navigateToAnalysisHistoryMenu,
        weatherForecastOverview = weatherForecastOverview,
        currentLocationNameProvider = { currentLocation?.name },
        newsItems = newsItems,
        ioTDataOverviewDui = ioTDataOverviewDui,
        isLoadingNewsItemsPreviewProvider = { isLoadingNewsItemsPreview },
        cocoaAverageSellPriceInfo = cocoaAverageSellPriceInfo
    )
}


@Composable
fun HomeContent(
    modifier: Modifier = Modifier,
    weatherForecastOverview: WeatherForecastOverviewDui? = null,
    currentLocationNameProvider: () -> String? = { null },
    navigateToNews: () -> Unit = {},
    navigateToShop: () -> Unit = {},
    navigateToWeather: () -> Unit = {},
    navigateToNewsDetail: (Int) -> Unit = {},
    analysisSessionPreviews: ImmutableList<AnalysisSessionPreviewDui> = persistentListOf(),
    navigateToDiagnosisResult: (Int) -> Unit = { _ -> },
    navigateToNotification: () -> Unit = {},
    navigateToAnalysisHistoryMenu: () -> Unit = { },
    cocoaAverageSellPriceInfo: CocoaAverageSellPriceInfoDui? = null,
    newsItems: ImmutableList<NewsItemDui> = persistentListOf(),
    ioTDataOverviewDui: IoTDataOverviewDui = IoTDataOverviewDui(),
    isLoadingNewsItemsPreviewProvider: () -> Boolean = { false }
) {

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        HomeHeaderSection(
            modifier = Modifier.fillMaxWidth(),
            navigateToNotification = navigateToNotification,
            weatherForecastOverview = weatherForecastOverview,
            currentLocationProvider = currentLocationNameProvider,
            navigateToWeather = navigateToWeather
        )

        IoTDevicesConnectivityStatusSection(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            isConnected = true
        )

        PriceInfoSection(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp, start = 16.dp, end = 16.dp),
            cocoaAverageSellPriceInfo = cocoaAverageSellPriceInfo
        )

        IoTDataOverviewSection(
            modifier = Modifier
                .fillMaxWidth(),
            navigateToNews = navigateToNews,
            navigateToShop = navigateToShop,
            navigateToWeather = navigateToWeather,
            ioTDataOverviewDui = ioTDataOverviewDui
        )

        Spacer(Modifier.height(8.dp))

        SectionHeadline(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(start = 16.dp, end = 2.dp),
            leadingIconResId = R.drawable.ic_camera_menu,
            trailingIconResId = R.drawable.ic_right_arrow,
            onTrailingIconClicked = navigateToAnalysisHistoryMenu,
            title = stringResource(R.string.riwayat_diagnosis)
        )

        Spacer(
            Modifier.height(2.dp)
        )

        val diagnosisHistoryListState = rememberLazyListState()

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            state = diagnosisHistoryListState
        ) {
            items(analysisSessionPreviews, { it.id }) { item ->
                HomeDiagnosisHistory(
                    modifier = Modifier
                        .clickable {
                            navigateToDiagnosisResult(item.id)
                        },
                    item = item
                )
            }
        }

        Spacer(
            Modifier.height(24.dp)
        )

        SectionHeadline(
            modifier = Modifier.padding(horizontal = 16.dp),
            title = stringResource(R.string.berita_mingguan_kamek),
            leadingIconResId = R.drawable.ic_news_menu
        )

        val configuration = LocalConfiguration.current

        val weeklyItemModifier = remember {
            Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 16.dp, end = 16.dp)
        }

        WeeklyNewsPreviewSection(
            modifier = weeklyItemModifier,
            newsItems = newsItems,
            isLoadingNewsItemsPreviewProvider = isLoadingNewsItemsPreviewProvider,
            onItemClicked = navigateToNewsDetail
        )

//        weeklyNewsItems.forEach {
//            key(it.id) {
//                WeeklyNews(
//                    modifier = weeklyItemModifier,
//                    item = it
//                )
//            }
//        }
    }
}


@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    KakaoXpertTheme {
        HomeContent()
    }
}
