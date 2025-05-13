package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.toplevel.diagnosishistory

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.neotelemetrixgdscunand.kakaoxpert.R
import com.neotelemetrixgdscunand.kakaoxpert.presentation.dui.AnalysisSessionPreviewDui
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Black10
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Grey90
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.KakaoXpertTheme
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.toplevel.ContentTopAppBar
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.toplevel.diagnosishistory.component.DiagnosisHistory
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.toplevel.diagnosishistory.component.ScrollUpButton
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.toplevel.diagnosishistory.component.SearchBar
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.toplevel.diagnosishistory.component.SearchCategory
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.toplevel.diagnosishistory.component.SearchHistoryCategory
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch

@Composable
fun DiagnosisScreen(
    modifier: Modifier = Modifier,
    viewModel: DiagnosisViewModel = hiltViewModel(),
    navigateToDiagnosisResult: (Int) -> Unit = { _ -> },
    navigateToTakePhoto: () -> Unit = {},
    bottomBarHeightPxProvider: () -> Int = { 0 }
) {

    val diagnosisHistories by viewModel.diagnosisHistoryPreview.collectAsState()

    DiagnosisContent(
        modifier = modifier,
        analysisSessionPreviews = diagnosisHistories,
        navigateToDiagnosisResult = navigateToDiagnosisResult,
        navigateToTakePhoto = navigateToTakePhoto,
        bottomBarHeightPxProvider = bottomBarHeightPxProvider
    )
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DiagnosisContent(
    modifier: Modifier = Modifier,
    analysisSessionPreviews: ImmutableList<AnalysisSessionPreviewDui> = persistentListOf(),
    navigateToDiagnosisResult: (Int) -> Unit = { _ -> },
    navigateToTakePhoto: () -> Unit = { },
    bottomBarHeightPxProvider: () -> Int = { 0 }
) {

    val selectedSearchHistoryCategory by remember {
        mutableStateOf(SearchHistoryCategory.ALL)
    }

    Box(
        Modifier.fillMaxSize()
    ) {

        val configuration = LocalConfiguration.current
        val density = LocalDensity.current

        val parentListState = rememberLazyListState()

        val initialHeightBeforeCalculating = -1
        var remainingHeightPx: Int by remember {
            mutableIntStateOf(initialHeightBeforeCalculating)
        }
        var topBarHeightPx by remember {
            mutableIntStateOf(initialHeightBeforeCalculating)
        }

        val lazyColumnModifier = remember {
            var previousList = persistentListOf<AnalysisSessionPreviewDui>()
                .toImmutableList()

            modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(color = Grey90)
                .onGloballyPositioned { coordinates ->
                    if (analysisSessionPreviews != previousList) return@onGloballyPositioned

                    val screenHeightPx = with(density) {
                        configuration.screenHeightDp.dp.roundToPx()
                    }
                    val remainingHeightIfAny = (screenHeightPx - coordinates.size.height)
                        .coerceAtLeast(0)

                    remainingHeightPx = remainingHeightIfAny

                    previousList = analysisSessionPreviews
                }
        }
        LazyColumn(
            modifier = lazyColumnModifier,
            state = parentListState,
        ) {
            item {
                ContentTopAppBar(
                    onTopAppBarHeightMeasured = {
                        topBarHeightPx = it
                    },
                    navigateToTakePhoto = navigateToTakePhoto
                )
            }

            stickyHeader {
                Column(
                    Modifier
                        .padding(horizontal = 16.dp)
                        .background(Grey90)
                        .wrapContentSize()
                ) {
                    Spacer(Modifier.height(16.dp))

                    Text(
                        stringResource(R.string.riwayat_diagnosis),
                        style = MaterialTheme.typography.headlineSmall,
                        color = Black10
                    )

                    Spacer(Modifier.height(16.dp))

                    val searchBarInteractionSource = remember {
                        MutableInteractionSource()
                    }

                    var searchQuery by remember {
                        mutableStateOf("")
                    }
                    SearchBar(
                        queryProvider = { searchQuery },
                        onQueryChange = {
                            searchQuery = it
                        },
                        hint = stringResource(R.string.cari_histori_hasil_diagnosis),
                        interactionSource = searchBarInteractionSource,
                    )

                    Spacer(Modifier.height(12.dp))


                }
                val listCategoryState = rememberLazyListState()
                LazyRow(
                    state = listCategoryState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = Grey90),
                    contentPadding = PaddingValues(start = 16.dp, bottom = 8.dp, end = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(SearchHistoryCategory.entries, key = { it.ordinal }) {
                        SearchCategory(
                            isSelected = it == selectedSearchHistoryCategory,
                            text = stringResource(it.textResId)
                        )
                    }
                }

                Spacer(Modifier.height(8.dp))
            }
            items(analysisSessionPreviews, key = { it.id }, contentType = { it::class }) { item ->
                DiagnosisHistory(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    item = item,
                    onClick = {
                        navigateToDiagnosisResult(item.id)
                    }
                )

                Spacer(Modifier.height(16.dp))
            }

            item {
                Spacer(
                    Modifier.height(
                        calculateContentBottomPaddingOfLazyColumn(
                            topBarHeightPx = topBarHeightPx,
                            bottomBarHeightPx = bottomBarHeightPxProvider(),
                            remainingContentHeight = remainingHeightPx,
                            density = density
                        )
                    )
                )
            }
        }


        val coroutineScope = rememberCoroutineScope()
        val onScrollUpButtonClicked: () -> Unit = remember {
            {
                coroutineScope.launch {
                    parentListState.scrollToItem(0)
                }
            }
        }
        ScrollUpButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .fillMaxHeight(0.15f)
                .fillMaxWidth(0.25f),
            lazyListStateProvider = { parentListState },
            onClick = onScrollUpButtonClicked
        )
    }
}

private fun calculateContentBottomPaddingOfLazyColumn(
    topBarHeightPx: Int,
    remainingContentHeight: Int,
    bottomBarHeightPx: Int = 0,
    density: Density
): Dp {
    val isContentHasFilledToMaxHeight = remainingContentHeight - bottomBarHeightPx <= 0

    if (isContentHasFilledToMaxHeight) return 32.dp

    val remainingHeightDp = with(density) {
        remainingContentHeight.toDp()
    }

    val topBarHeightDp = with(density) {
        topBarHeightPx.toDp()
    }

    val bottomBarHeightDp = with(density) {
        bottomBarHeightPx.toDp()
    }

    return remainingHeightDp - bottomBarHeightDp + topBarHeightDp
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F5F5)
@Composable
private fun DiagnosisScreenPreview() {
    KakaoXpertTheme {
        DiagnosisContent(
            analysisSessionPreviews = List(10) {
                AnalysisSessionPreviewDui(
                    id = it,
                    title = "Example",
                    imageUrlOrPath = "",
                    date = "12-04-2025",
                    predictedPrice = 1400f
                )
            }.take(1).toImmutableList()
        )
    }

}
