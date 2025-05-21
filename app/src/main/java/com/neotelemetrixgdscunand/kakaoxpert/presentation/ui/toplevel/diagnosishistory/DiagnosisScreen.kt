package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.toplevel.diagnosishistory

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.neotelemetrixgdscunand.kakaoxpert.R
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.SearchAnalysisHistoryCategory
import com.neotelemetrixgdscunand.kakaoxpert.presentation.dui.AnalysisSessionPreviewDui
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Black10
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Grey90
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.KakaoXpertTheme
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.toplevel.ContentTopAppBar
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.toplevel.diagnosishistory.component.DiagnosisHistory
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.toplevel.diagnosishistory.component.ScrollUpButton
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.toplevel.diagnosishistory.component.SearchBar
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.toplevel.diagnosishistory.component.SearchCategory
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

@Composable
fun DiagnosisScreen(
    modifier: Modifier = Modifier,
    viewModel: DiagnosisViewModel = hiltViewModel(),
    navigateToDiagnosisResult: (Int) -> Unit = { _ -> },
    navigateToTakePhoto: () -> Unit = {},
    bottomBarHeightPxProvider: () -> Int = { 0 }
) {

    val diagnosisHistories = viewModel.diagnosisHistoryPreview.collectAsLazyPagingItems()
    val searchBarQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val selectedSearchCategory by viewModel.selectedSearchCategory.collectAsStateWithLifecycle()

    DiagnosisContent(
        modifier = modifier,
        analysisSessionPreviews = diagnosisHistories,
        navigateToDiagnosisResult = navigateToDiagnosisResult,
        navigateToTakePhoto = navigateToTakePhoto,
        bottomBarHeightPxProvider = bottomBarHeightPxProvider,
        searchBarQueryProvider = { searchBarQuery },
        onSearchBarQueryChange = viewModel::onSearchBarQueryChange,
        selectedSearchAnalysisHistoryCategoryProvider = { selectedSearchCategory },
        onSelectedSearchAnalysisCategoryChanged = viewModel::onSelectedCategoryChanged
    )
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DiagnosisContent(
    modifier: Modifier = Modifier,
    navigateToDiagnosisResult: (Int) -> Unit = { _ -> },
    navigateToTakePhoto: () -> Unit = { },
    bottomBarHeightPxProvider: () -> Int = { 0 },
    searchBarQueryProvider: () -> String = { "" },
    onSearchBarQueryChange: (String) -> Unit = { },
    analysisSessionPreviews: LazyPagingItems<AnalysisSessionPreviewDui>,
    selectedSearchAnalysisHistoryCategoryProvider: () -> SearchAnalysisHistoryCategory = { SearchAnalysisHistoryCategory.ALL },
    onSelectedSearchAnalysisCategoryChanged: (SearchAnalysisHistoryCategory) -> Unit = { }
) {

    Box(
        Modifier.fillMaxSize()
    ) {

        val configuration = LocalConfiguration.current
        val density = LocalDensity.current
        val focusManager = LocalFocusManager.current

        val parentListState = rememberLazyListState()
        val coroutineScope = rememberCoroutineScope()

        val searchBarInteractionSource = remember {
            MutableInteractionSource()
        }
        val maxQueryLength = 30
        val searchBarPositionInList = 1
        var scrollSearchBarToTopJob: Job? = remember { null }
        val onSearchBarTapped: () -> Unit = remember {
            {
                scrollSearchBarToTopJob?.cancel()
                scrollSearchBarToTopJob = coroutineScope.launch {
                    parentListState.scrollToItem(searchBarPositionInList)
                }
            }
        }

        val isEmptyInfoShown by remember {
            derivedStateOf {
                analysisSessionPreviews.itemCount == 0
            }
        }

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
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        focusManager.clearFocus()
                    })
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

                    SearchBar(
                        queryProvider = searchBarQueryProvider,
                        onQueryChange = { newQuery ->
                            if (newQuery.length <= maxQueryLength) {
                                onSearchBarQueryChange(newQuery)
                            }
                        },
                        hint = stringResource(R.string.cari_histori_hasil_diagnosis),
                        interactionSource = searchBarInteractionSource,
                        onTextFieldTapped = onSearchBarTapped
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
                    items(SearchAnalysisHistoryCategory.entries, key = { it.ordinal }) {
                        SearchCategory(
                            modifier = Modifier
                                .clickable(onClick = {
                                    focusManager.clearFocus()
                                    onSelectedSearchAnalysisCategoryChanged(it)
                                }),
                            isSelected = it == selectedSearchAnalysisHistoryCategoryProvider(),
                            text = it.getTabText().getValue()
                        )
                    }
                }

                Spacer(Modifier.height(8.dp))
            }

            if (isEmptyInfoShown) {
                item {
                    Column(Modifier.fillMaxWidth()) {
                        Spacer(Modifier.height(32.dp))
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = stringResource(R.string.tidak_ada_item_yang_ditemukan),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }
            } else {
                items(
                    analysisSessionPreviews.itemCount,
                    key = { index: Int -> analysisSessionPreviews[index]?.id ?: -1 }) {
                    val item = analysisSessionPreviews[it] ?: return@items
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
        val pagingData = remember {
            PagingData.from(
                List(10) {
                    AnalysisSessionPreviewDui(
                        id = it,
                        title = "Example",
                        imageUrlOrPath = "",
                        date = "12-04-2025",
                        predictedPrice = 1400f,
                        hasSynced = true,
                        availableOffline = true
                    )
                }.take(1).toImmutableList()
            )
        }

        val flowPagingData: Flow<PagingData<AnalysisSessionPreviewDui>> =
            remember { flow { pagingData } }
        DiagnosisContent(
            analysisSessionPreviews = flowPagingData.collectAsLazyPagingItems()
        )
    }

}
