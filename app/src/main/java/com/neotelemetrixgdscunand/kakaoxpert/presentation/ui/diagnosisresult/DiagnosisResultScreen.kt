package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.diagnosisresult

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.neotelemetrixgdscunand.kakaoxpert.R
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.CocoaDisease
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.DetectedCocoa
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.getDetectedDiseaseCacaos
import com.neotelemetrixgdscunand.kakaoxpert.presentation.dui.DiagnosisSessionDui
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Grey90
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.KakaoXpertTheme
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.cacaoimagedetail.components.OverlayCompose
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.diagnosisresult.component.DiagnosisResultTabSection
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.diagnosisresult.component.NavigateUpButton
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.diagnosisresult.diseasediagnosis.DiagnosisDiseaseTabScreen
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.diagnosisresult.diseasediagnosis.compoenent.DiagnosisResultHeaderSection
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.diagnosisresult.priceanalysis.PriceAnalysisTabScreen
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.diagnosisresult.util.getBoundingBoxWithItsNameAsTheLabel
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.util.AsyncImagePainterStable
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.util.collectChannelWhenStarted
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun DiagnosisResultScreen(
    modifier: Modifier = Modifier,
    viewModel: DiagnosisResultViewModel = hiltViewModel(),
    navigateUp: () -> Unit = {},
    showSnackbar: (String) -> Unit = { },
    navigateToCacaoImageDetail: (Int, Int?, String) -> Unit = { _, _, _ -> },
) {

    val uiState by viewModel.uiState.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    LaunchedEffect(true) {
        lifecycleOwner.collectChannelWhenStarted(
            viewModel.event
        ) { event ->
            when (event) {
                is AnalysisResultUIEvent.OnFailedToAnalyseImage -> {
                    val message =
                        context.getString(R.string.sepertinya_ada_kesalahan_coba_lagi_nanti)
                    showSnackbar(message)
                    navigateUp()
                }

                AnalysisResultUIEvent.OnInputImageInvalid -> {
                    val message =
                        context.getString(R.string.input_image_is_invalid_no_cocoa_detected)
                    showSnackbar(message)
                    navigateUp()
                }
            }
        }
    }

    DiagnosisResultContent(
        modifier = modifier,
        navigateUp = navigateUp,
        uiState = uiState,
        navigateToCacaoImageDetail = { detectedCacaoId ->
            uiState.imagePreviewPath?.apply {
                navigateToCacaoImageDetail(
                    uiState.diagnosisSession.id,
                    detectedCacaoId,
                    this
                )
            }
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiagnosisResultContent(
    modifier: Modifier = Modifier,
    navigateUp: () -> Unit = {},
    uiState: DiagnosisResultUIState = DiagnosisResultUIState(),
    navigateToCacaoImageDetail: (Int?) -> Unit = { }
) {

    val imageAspectRatio = 1.26f
    val configuration = LocalConfiguration.current


    val topAppBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
        topAppBarState
    )

    val topAppBarHeightDp = remember {
        configuration.screenHeightDp.dp / 3
    }

    val density = LocalDensity.current
    val isLocalNavigateUpButtonVisible by remember {
        derivedStateOf {
            //To check if Top App Bar has collapsed
            topAppBarState.heightOffset.roundToInt() <= with(density) {
                -(topAppBarHeightDp.roundToPx())
            }
        }
    }

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        containerColor = Grey90,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors().copy(
                    containerColor = Grey90,
                    scrolledContainerColor = Grey90
                ),
                title = { },
                scrollBehavior = scrollBehavior,
                expandedHeight = topAppBarHeightDp,
                actions = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = Grey90)
                            .aspectRatio(imageAspectRatio)
                    ) {

                        AsyncImagePainterStable(
                            modifier = Modifier
                                .fillMaxSize()
                                .align(Alignment.Center)
                                .clickable {
                                    if (!uiState.isLoading) navigateToCacaoImageDetail(null)
                                },
                            placeholderResId = R.drawable.ic_camera,
                            imageUrlOrPath = uiState.imagePreviewPath,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                        )

                        val context = LocalContext.current
                        val adjustedBoundingBox = remember(uiState.diagnosisSession) {
                            uiState.diagnosisSession.detectedCocoas.map {
                                it.getBoundingBoxWithItsNameAsTheLabel(context)
                            }
                        }

                        OverlayCompose(
                            modifier = Modifier
                                .fillMaxSize()
                                .align(Alignment.Center),
                            boundingBoxes = adjustedBoundingBox
                        )

                        TopAppBarNavigateUpButtonWrapper(
                            modifier = Modifier.align(Alignment.TopStart),
                            navigateUp = navigateUp,
                            isLocalNavigateUpButtonVisibleProvider = { isLocalNavigateUpButtonVisible }
                        )
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

            DiagnosisResultContentBody(
                diagnosisSessionDui = uiState.diagnosisSession,
                isLoadingProvider = { uiState.isLoading },
                navigateToCacaoImageDetail = navigateToCacaoImageDetail,
                isLocalNavigateUpButtonVisibleProvider = { isLocalNavigateUpButtonVisible },
                navigateUp = navigateUp
            )

        }

    }
}

@Composable
fun TopAppBarNavigateUpButtonWrapper(
    modifier: Modifier = Modifier,
    isLocalNavigateUpButtonVisibleProvider: () -> Boolean = { false },
    navigateUp: () -> Unit = { }
) {
    val topToArrowMarginRatio = 0.04571f

    val configuration = LocalConfiguration.current
    val topToArrowMargin = remember {
        configuration.screenHeightDp.dp * topToArrowMarginRatio
    }

    if (!isLocalNavigateUpButtonVisibleProvider()) {
        NavigateUpButton(
            modifier = modifier,
            topToArrowMargin = topToArrowMargin,
            navigateUp = navigateUp
        )
    }

}

@Composable
fun DiagnosisResultContentBody(
    diagnosisSessionDui: DiagnosisSessionDui = DiagnosisSessionDui(),
    isLoadingProvider: () -> Boolean = { false },
    isLocalNavigateUpButtonVisibleProvider: () -> Boolean = { false },
    navigateToCacaoImageDetail: (Int?) -> Unit,
    navigateUp: () -> Unit = {}
) {
    val groupedDetectedDisease: ImmutableMap<CocoaDisease, ImmutableList<DetectedCocoa>> =
        remember(diagnosisSessionDui) {
            val map = mutableMapOf<CocoaDisease, ImmutableList<DetectedCocoa>>()
            diagnosisSessionDui.detectedCocoas.groupBy {
                it.disease
            }.map {
                val (cocoaDisease, list) = it.toPair()
                map[cocoaDisease] = list.toImmutableList()
            }
            map.toImmutableMap()
        }

    val isExpandList = remember(groupedDetectedDisease) {
        List(groupedDetectedDisease.keys.size) { index ->
            val isInitialStateExpand = index == 0
            mutableStateOf(isInitialStateExpand)
        }.toImmutableList()
    }

    var isDiagnosisTabSelected by rememberSaveable { mutableStateOf(true) }

    DiagnosisResultHeaderSection(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, top = 16.dp, end = 16.dp),
        sessionName = diagnosisSessionDui.title,
        isLoadingProvider = isLoadingProvider,
        isLocalNavigateUpButtonVisibleProvider = isLocalNavigateUpButtonVisibleProvider,
        navigateUp = navigateUp
    )

    val coroutineScope = rememberCoroutineScope()
    val diagnosisDiseaseColumnScrollState = rememberScrollState()
    val priceAnalysisColumnScrollState = rememberScrollState()

    val changeSelectedTab: (Boolean) -> Unit = remember {
        {
            val isReselectedTab = it == isDiagnosisTabSelected
            if (!isReselectedTab) {
                isDiagnosisTabSelected = it
                coroutineScope.launch {
                    if (isDiagnosisTabSelected) {
                        diagnosisDiseaseColumnScrollState.scrollTo(0)
                    } else priceAnalysisColumnScrollState.scrollTo(0)
                }
            }
        }
    }

    DiagnosisResultTabSection(
        isDiagnosisTabSelected = isDiagnosisTabSelected,
        changeSelectedTab = changeSelectedTab
    )

    if (isDiagnosisTabSelected) {
        Column(
            Modifier
                .verticalScroll(diagnosisDiseaseColumnScrollState)
        ) {
            DiagnosisDiseaseTabScreen(
                groupedDetectedDisease = groupedDetectedDisease,
                toggleItemExpand = { index ->
                    isExpandList[index].value = !isExpandList[index].value
                },
                isItemExpandProvider = { index ->
                    isExpandList[index].value
                },
                isLoadingProvider = isLoadingProvider,
                navigateToCacaoImageDetail = navigateToCacaoImageDetail,
                diagnosisSessionDui = diagnosisSessionDui
            )
        }


    } else {
        Column(
            Modifier.verticalScroll(priceAnalysisColumnScrollState)
        ) {
            PriceAnalysisTabScreen(
                isLoadingProvider = isLoadingProvider,
                navigateToCacaoImageDetail = navigateToCacaoImageDetail,
                groupedDetectedDisease = groupedDetectedDisease,
            )
        }
    }
}

@Preview(showBackground = true, heightDp = 2000)
@Composable
private fun DiagnosisResultScreenPreview() {
    KakaoXpertTheme {
        DiagnosisResultContent(
            uiState = DiagnosisResultUIState(
                diagnosisSession = DiagnosisSessionDui(
                    id = 0,
                    title = "Sampel Kakao Pak Tono",
                    imageUrlOrPath = "https://lh3.googleusercontent.com/fife/ALs6j_FBmyzFhcT48JsWTnlztAKHZJVIxq_KWTNbYFRO8aOkqiJtsH0HDNSDjaqO-1SrWvE8AR85sU2QcU93FD_OWlJ_xZQsL4JpmKESvrTSvPr37FzRfsudWwBrs6HPawtN9qXiqM6JRd4htsh1cuv4SUajVBpjwkIuUVFhfdmMgV-qlZsqn7Ui_W2zj3Kf2Z6BuAeQ-rxzhVoMWxsiXxZL1OVHRz4permiV_G8nOHZLLLbJ_hwKLNHpnFPH15efQarYQtdrfiTXwcYSC_F5_sEasYb-IgUrr1o-_T-__wrnZSc6GEF9YUxQcv2urAt5EqcPdGumJ9GWToq9AtYz9XfD5zB0rsBNl2GhHn23p8DIkq3NCJNUM4hGXSVBLIi5GmBxZXnKlox4vKRarRvtzipvfCpd-iCm6i-66uvx31ghCU2LH5DrqjaPB5-hL30W20F7vbzmHHf2j78Hd6wM7xlhptZChs7_AzZxq9fZ_D9dAAkXaZ9uWuKvKddSFiaajWNapI2mcOae_QKEraLv2t41uxjxAhz1kEhgERN_ZQXAuTpaCudlpaqZRa88LkVwdrmEsfcgAcLAWiEnvcwWJNhjD-B7hWMOx4NvxC_Nz3MAhflOI4-i1u4HDkY6GsZ3mBpzaWiKY6Po6tkjJx1UMTozff4YmDK1W59wqd-YZ8ZQkLCjrRJO_FIkVo9RTaQm-kY_4pCz2f4PjWPbVxsUUiHVQJEB-nus4cK2kGS3DMheO11j2i_EcnuEBXH69fazqZFTU9ahQ4_fARXMxn_k30h5Nkl7YiLMW_R3YK4huwaJUCxwhTTv3vWMeQDkSWOTI2lnks1soQyPRRaBCn3tFYY0BlaR9Gdz__3OrXtFECyGcqVfOEuY-tvGtqwQfHBXNG5XqN8e28CM7Pt2f0gFk3ha9K1m6DOJR2dkLy9monvnx_L4UEPfGWIMa2pnSPOORePp9v_nK380f2t15GPjtpznc4ezON3dHidJV3pkwXpsgXQf0gOjSBWdSf8GtIDoHALK0BpPb5nW0nTMkw7bunhDhk9BCB11WjzbsTLi-Tlau17NQAcFaJE6EQlz0ujZsKMK6oshGveVET6gt7480hPoiGo0LCarO8gDXR8Y6QQDY6mearThrt9xquxCwff-SS-sleeJMk9hqahiAHiureTtT_lwKOJIscCZA7PkDy22tx-gCyquIIh0N3layVLsKX-y07p3Bu18y7KFU1Ld9-RA7IfX6aaOyGyqJMDswpiIytQyQXh9cRlCX0Qjn81YNKmQurktSRZ7c99ht17JBo5dGdmXlVk87kHVthwjqygGU9slR6OhgNW0nOHEgDVS-RDv_7Cy3sA55Vs9P82RO8qC1WW7zlUV5gIETD0o_mgy5zBKEgWRhKixYnSoAuXodVNvUNpG4u7Df29GJkq_exP_iOL0OGS3qPMneG_OC6V9Uez4Jd9nVrNpCnySAXksOGNYUiwt_KkOsc98VRAsb5jV5qrd3ad-bziRcIci09WjDNbMjKJ89bO8yZV8DMCJd0uOVRp3DDHRLh8l5vCha8gtLQqLcMV8htomJtJy7hME1v-C8v9iXt6rIGoRRgYOfnnDNQCwBnRhUxA4WSNMbqnw_qQ2F6LEAODfhJfXfR_di9OyPh2q3K9h3mS=w1920-h927",
                    date = "9-11-2024",
                    predictedPrice = 1200f,
                    detectedCocoas = getDetectedDiseaseCacaos().toImmutableList()
                )
            )
        )
    }
}

//@Preview(showBackground = true, heightDp = 2000)
//@Composable
//private fun DiagnosisResultScreenPreview() {
//    KamekAppTheme {
//        DiagnosisResultContent(
//            uiState = DiagnosisResultUIState(
//                diagnosisSession = DiagnosisSessionComposeStable(
//                    id = 0,
//                    title = "Sampel Kakao Pak Tono",
//                    imageUrlOrPath = "https://lh3.googleusercontent.com/fife/ALs6j_FBmyzFhcT48JsWTnlztAKHZJVIxq_KWTNbYFRO8aOkqiJtsH0HDNSDjaqO-1SrWvE8AR85sU2QcU93FD_OWlJ_xZQsL4JpmKESvrTSvPr37FzRfsudWwBrs6HPawtN9qXiqM6JRd4htsh1cuv4SUajVBpjwkIuUVFhfdmMgV-qlZsqn7Ui_W2zj3Kf2Z6BuAeQ-rxzhVoMWxsiXxZL1OVHRz4permiV_G8nOHZLLLbJ_hwKLNHpnFPH15efQarYQtdrfiTXwcYSC_F5_sEasYb-IgUrr1o-_T-__wrnZSc6GEF9YUxQcv2urAt5EqcPdGumJ9GWToq9AtYz9XfD5zB0rsBNl2GhHn23p8DIkq3NCJNUM4hGXSVBLIi5GmBxZXnKlox4vKRarRvtzipvfCpd-iCm6i-66uvx31ghCU2LH5DrqjaPB5-hL30W20F7vbzmHHf2j78Hd6wM7xlhptZChs7_AzZxq9fZ_D9dAAkXaZ9uWuKvKddSFiaajWNapI2mcOae_QKEraLv2t41uxjxAhz1kEhgERN_ZQXAuTpaCudlpaqZRa88LkVwdrmEsfcgAcLAWiEnvcwWJNhjD-B7hWMOx4NvxC_Nz3MAhflOI4-i1u4HDkY6GsZ3mBpzaWiKY6Po6tkjJx1UMTozff4YmDK1W59wqd-YZ8ZQkLCjrRJO_FIkVo9RTaQm-kY_4pCz2f4PjWPbVxsUUiHVQJEB-nus4cK2kGS3DMheO11j2i_EcnuEBXH69fazqZFTU9ahQ4_fARXMxn_k30h5Nkl7YiLMW_R3YK4huwaJUCxwhTTv3vWMeQDkSWOTI2lnks1soQyPRRaBCn3tFYY0BlaR9Gdz__3OrXtFECyGcqVfOEuY-tvGtqwQfHBXNG5XqN8e28CM7Pt2f0gFk3ha9K1m6DOJR2dkLy9monvnx_L4UEPfGWIMa2pnSPOORePp9v_nK380f2t15GPjtpznc4ezON3dHidJV3pkwXpsgXQf0gOjSBWdSf8GtIDoHALK0BpPb5nW0nTMkw7bunhDhk9BCB11WjzbsTLi-Tlau17NQAcFaJE6EQlz0ujZsKMK6oshGveVET6gt7480hPoiGo0LCarO8gDXR8Y6QQDY6mearThrt9xquxCwff-SS-sleeJMk9hqahiAHiureTtT_lwKOJIscCZA7PkDy22tx-gCyquIIh0N3layVLsKX-y07p3Bu18y7KFU1Ld9-RA7IfX6aaOyGyqJMDswpiIytQyQXh9cRlCX0Qjn81YNKmQurktSRZ7c99ht17JBo5dGdmXlVk87kHVthwjqygGU9slR6OhgNW0nOHEgDVS-RDv_7Cy3sA55Vs9P82RO8qC1WW7zlUV5gIETD0o_mgy5zBKEgWRhKixYnSoAuXodVNvUNpG4u7Df29GJkq_exP_iOL0OGS3qPMneG_OC6V9Uez4Jd9nVrNpCnySAXksOGNYUiwt_KkOsc98VRAsb5jV5qrd3ad-bziRcIci09WjDNbMjKJ89bO8yZV8DMCJd0uOVRp3DDHRLh8l5vCha8gtLQqLcMV8htomJtJy7hME1v-C8v9iXt6rIGoRRgYOfnnDNQCwBnRhUxA4WSNMbqnw_qQ2F6LEAODfhJfXfR_di9OyPh2q3K9h3mS=w1920-h927",
//                    date = "9-11-2024",
//                    predictedPrice = 1200f,
//                    detectedCacaos = getDetectedDiseaseCacaos().toImmutableList()
//                )
//            )
//        )
//    }
//}

//@OptIn(ExperimentalFoundationApi::class)
//@Composable
//fun DiagnosisResultContent(
//    modifier: Modifier = Modifier,
//    navigateUp: () -> Unit = {},
//    uiState: DiagnosisResultUIState = DiagnosisResultUIState(),
//    navigateToCacaoImageDetail: (Int?) -> Unit = { }
//) {
//
//    val imageAspectRatio = 1.26f
//    val topToArrowMarginRatio = 0.04571f
//    val listState = rememberLazyListState()
//
//    val configuration = LocalConfiguration.current
//    val topToArrowMargin = remember {
//        configuration.screenHeightDp.dp * topToArrowMarginRatio
//    }
//
//    val groupedDetectedDisease:ImmutableMap<CacaoDisease, ImmutableList<DetectedCacao>> = remember(uiState.diagnosisSession) {
//        val map = mutableMapOf<CacaoDisease, ImmutableList<DetectedCacao>>()
//        uiState.diagnosisSession.detectedCacaos.groupBy {
//            it.disease
//        }.map {
//            val (cacaoDisease, list) = it.toPair()
//            map[cacaoDisease] = list.toImmutableList()
//        }
//        map.toImmutableMap()
//    }
//
//    val groupedDetectedDiseaseKeys = remember(groupedDetectedDisease) {
//        groupedDetectedDisease.keys.toList()
//    }
//
//    val isExpandList = rememberSaveable(groupedDetectedDiseaseKeys) {
//        List(groupedDetectedDiseaseKeys.size) { index ->
//            val isInitialStateExpand = index == 0
//            mutableStateOf(isInitialStateExpand)
//        }
//    }
//
//    val outermostPaddingModifier = remember {
//        Modifier
//            .fillMaxWidth()
//            .padding(horizontal = 16.dp)
//    }
//
//    val damageLevelCategoryInfo = remember {
//        listOf(
//            DamageLevelCategory.Low,
//            DamageLevelCategory.Medium,
//            DamageLevelCategory.High
//        )
//    }
//
//    val bulletNumberModifier = remember {
//        Modifier
//            .wrapContentSize()
//            .clip(CircleShape)
//            .background(color = Maroon55)
//            .sizeIn(minWidth = 24.dp, minHeight = 24.dp)
//    }
//
//    var isDiagnosisTabSelected by rememberSaveable { mutableStateOf(true) }
//
//    Box {
//        LazyColumn(
//            modifier = modifier
//                .align(Alignment.TopCenter)
//                .background(color = Grey90),
//            state = listState
//        ) {
//            item {
//                Box(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .aspectRatio(imageAspectRatio)
//                ) {
//
//                    val image = rememberAsyncImagePainter(
//                        model = uiState.imagePreviewPath,
//                        placeholder = painterResource(R.drawable.ic_camera),
//                        contentScale = ContentScale.Crop
//                    )
//
//                    Image(
//                        modifier = Modifier
//                            .fillMaxSize()
//                            .align(Alignment.Center)
//                            .clickable {
//                                navigateToCacaoImageDetail(null)
//                            },
//                        painter = image,
//                        contentDescription = null,
//                        contentScale = ContentScale.Crop,
//                    )
//
//                    val context = LocalContext.current
//                    val adjustedBoundingBox = remember(uiState.diagnosisSession) {
//                        uiState.diagnosisSession.detectedCacaos.map {
//                            it.getBoundingBoxWithItsNameAsTheLabel(context)
//                        }
//                    }
//
//                    OverlayCompose(
//                        modifier = Modifier
//                            .fillMaxSize()
//                            .align(Alignment.Center),
//                        boundingBoxes = adjustedBoundingBox
//                    )
//                }
//            }
//
//            item {
//                if (uiState.isLoading) {
//                    DiagnosisResultHeaderSectionLoading(
//                        Modifier
//                            .fillMaxWidth()
//                            .padding(start = 16.dp, top = 24.dp, end = 16.dp),
//                        sessionName = uiState.diagnosisSession.title
//                    )
//                } else DiagnosisResultHeaderSection(
//                    Modifier
//                        .fillMaxWidth()
//                        .padding(start = 16.dp, top = 24.dp, end = 16.dp),
//                    sessionName = uiState.diagnosisSession.title
//                )
//            }
//
//            stickyHeader {
//                DiagnosisResultTabSection(
//                    isDiagnosisTabSelected = isDiagnosisTabSelected,
//                    changeSelectedTab = {
//                        val isReselectedTab = it == isDiagnosisTabSelected
//                        if (!isReselectedTab) isDiagnosisTabSelected = it
//                    }
//                )
//            }
//
//            if (isDiagnosisTabSelected) {
//                item {
//                    Column(
//                        modifier
//                            .fillMaxWidth()
//                            .padding(start = 16.dp, end = 16.dp)
//                            .background(
//                                color = Color.White,
//                                shape = RoundedCornerShape(topEnd = 8.dp, topStart = 8.dp)
//                            )
//                            .padding(top = 16.dp, start = 16.dp, end = 16.dp),
//                    ) {
//                        Text(
//                            stringResource(R.string.penyakit_hama_yang_terdeteksi),
//                            style = MaterialTheme.typography.titleMedium,
//                            color = Black10
//                        )
//                        Spacer(Modifier.height(16.dp))
//
//                    }
//                }
//
//                itemsIndexed(
//                    items = groupedDetectedDiseaseKeys,
//                    key = { _, it -> it }) { index, it ->
//
//                    Column(
//                        modifier
//                            .fillMaxWidth()
//                            .padding(start = 16.dp, end = 16.dp)
//                            .then(
//                                if (index == groupedDetectedDiseaseKeys.lastIndex) {
//                                    Modifier.background(
//                                        color = Color.White,
//                                        shape = RoundedCornerShape(
//                                            bottomEnd = 8.dp,
//                                            bottomStart = 8.dp
//                                        )
//                                    )
//                                } else {
//                                    Modifier.background(
//                                        color = Color.White
//                                    )
//                                }
//                            )
//                            .padding(start = 16.dp, end = 16.dp),
//                    ) {
//                        Row(
//                            verticalAlignment = Alignment.CenterVertically
//                        ) {
//                            Box(
//                                contentAlignment = Alignment.Center,
//                                modifier = bulletNumberModifier
//                            ) {
//                                Text(
//                                    "${index + 1}",
//                                    style = MaterialTheme.typography.labelMedium,
//                                    color = Color.White
//                                )
//                            }
//
//                            Spacer(Modifier.width(8.dp))
//
//                            Text(
//                                text = stringResource(it.nameResId),
//                                style = MaterialTheme.typography.labelMedium.copy(
//                                    fontStyle = FontStyle.Italic
//                                ),
//                                color = Orange90
//                            )
//                        }
//
//                        Spacer(Modifier.height(12.dp))
//
//                        DetectedCacaoImageGrid(
//                            detectedCacaos = groupedDetectedDisease[it] ?: persistentListOf(),
//                            onItemClicked = navigateToCacaoImageDetail
//                        )
//
//                        if (index != groupedDetectedDiseaseKeys.lastIndex) {
//                            Spacer(Modifier.height(8.dp))
//                        }
//
//                    }
//
//                }
//
//                item {
//                    Spacer(Modifier.height(16.dp))
//
//                    val context = LocalContext.current
//                    if (uiState.isLoading) DiagnosisBottomContentLoading() else DiagnosisBottomContent(
//                        preventions = remember(uiState.diagnosisSession) {
//                            uiState.diagnosisSession.detectedCacaos.firstOrNull()?.disease?.controlStringResId?.map {
//                                context.getString(it)
//                            }?.toImmutableList() ?: persistentListOf()
//                        },
//                        solution = uiState.diagnosisSession.detectedCacaos.firstOrNull()?.disease?.solutionStringResId?.let {
//                            stringResource(it)
//                        } ?: ""
//                    )
//                }
//
//                item {
//                    Text(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(horizontal = 16.dp),
//                        text = stringResource(R.string.rincian_penyakit),
//                        style = MaterialTheme.typography.titleMedium
//                    )
//
//                    Spacer(Modifier.height(16.dp))
//                }
//
//
//                if (uiState.isLoading) {
//                    item {
//                        DiagnosisDiseaseDetailsLoading()
//                    }
//                } else {
//                    itemsIndexed(
//                        items = groupedDetectedDiseaseKeys,
//                        key = { _, it -> it.ordinal }) { index, diseaseKey ->
//
//                        DiagnosisDiseaseDetails(
//                            isExpand = isExpandList[index].value,
//                            toggleExpand = {
//                                isExpandList[index].value = !isExpandList[index].value
//                            },
//                            modifier = Modifier.padding(bottom = 8.dp),
//                            diseaseName = stringResource(diseaseKey.nameResId),
//                            detectedCacaos = groupedDetectedDisease[diseaseKey] ?: persistentListOf(),
//                            diseaseCause = stringResource(diseaseKey.causeStringResId),
//                            diseaseSymptoms = stringResource(diseaseKey.symptomStringResId),
//                            seedCondition = stringResource(diseaseKey.seedConditionStringResId),
//                            onDetectedCacaoImageClicked = navigateToCacaoImageDetail
//                        )
//                    }
//                }
//
//
//            } else {
//                if (uiState.isLoading) {
//                    item {
//                        PriceAnalysisContentLoading()
//                    }
//                } else {
//                    item {
//                        Column(
//                            Modifier
//                                .fillMaxWidth()
//                                .padding(horizontal = 16.dp)
//                                .background(color = Yellow90, shape = RoundedCornerShape(8.dp))
//                                .padding(16.dp)
//                        ) {
//                            Row(
//                                Modifier.fillMaxWidth(),
//                                verticalAlignment = Alignment.Top
//                            ) {
//                                Image(
//                                    painter = painterResource(R.drawable.ic_info),
//                                    contentScale = ContentScale.Fit,
//                                    contentDescription = null,
//                                    modifier = Modifier.size(20.dp)
//                                )
//
//                                Spacer(Modifier.width(16.dp))
//
//                                Text(
//                                    stringResource(R.string.price_note),
//                                    style = MaterialTheme.typography.labelMedium,
//                                    color = Maroon55,
//                                    textAlign = TextAlign.Start
//                                )
//                            }
//
//                            Spacer(Modifier.height(24.dp))
//
//                            Text(
//                                stringResource(R.string.klik_untuk_prediksi_harga_dengan_porsi_yang_berbeda),
//                                style = MaterialTheme.typography.titleMedium.copy(
//                                    textDecoration = TextDecoration.Underline,
//                                    letterSpacing = (-0.175).sp
//                                ),
//                                color = Orange90,
//                                textAlign = TextAlign.Start
//                            )
//                        }
//                    }
//
//                    item {
//                        Spacer(Modifier.height(16.dp))
//
//                        PriceAnalysisOverview()
//                    }
//
//                    item {
//                        Spacer(Modifier.height(16.dp))
//
//                        Text(
//                            stringResource(R.string.rincian_prediksi_harga),
//                            style = MaterialTheme.typography.titleMedium,
//                            color = Black10,
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(horizontal = 16.dp)
//                        )
//
//                        Spacer(Modifier.height(16.dp))
//                    }
//
//                    items(
//                        count = 3,
//                        key = { it },
//                        itemContent = {
//                            PriceAnalysisContent(
//                                modifier = outermostPaddingModifier,
//                                damageLevelCategory = damageLevelCategoryInfo[it],
//                                groupedDetectedDisease = groupedDetectedDisease,
//                                onDetectedCacaoImageClicked = navigateToCacaoImageDetail
//                            )
//                            Spacer(Modifier.height(16.dp))
//                        }
//                    )
//                }
//            }
//        }
//
//        NavigateUpButton(
//            modifier = Modifier
//                .align(Alignment.TopStart),
//            topToArrowMargin = topToArrowMargin,
//            navigateUp = navigateUp
//        )
//
//    }
//}