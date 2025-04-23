package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.news

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.neotelemetrixgdscunand.kakaoxpert.R
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Black10
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Grey60
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Grey90
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.KakaoXpertTheme
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Green55
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.toplevel.diagnosishistory.component.ScrollUpButton
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.toplevel.diagnosishistory.component.SearchBar
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.toplevel.diagnosishistory.component.SearchCategory
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.toplevel.home.component.WeeklyNews
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.toplevel.home.component.getDummyWeeklyNewsItems
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.util.ImagePainterStable
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun NewsScreen(
    modifier: Modifier = Modifier,
    navigateUp: () -> Unit = {},
    navigateToDetail: () -> Unit = {}
) {
    val configuration = LocalConfiguration.current

    Box(Modifier.background(Grey90)) {
        val imageBackgroundSize = remember {
            val imageAspectRatio = when (configuration.orientation) {
                Configuration.ORIENTATION_PORTRAIT -> 0.205f
                else -> 0.5f
            }
            (configuration.screenHeightDp * imageAspectRatio).dp
        }

        ImagePainterStable(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .width(imageBackgroundSize)
                .aspectRatio(0.655f),
            drawableResId = R.drawable.news_bg,
            contentScale = ContentScale.FillBounds,
            contentDescription = null
        )

        val listState = rememberLazyListState()

        val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

        Scaffold(
            containerColor = Color.Transparent,
            modifier = Modifier
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                val density = LocalDensity.current
                var expandedHeightPx by remember {
                    val screenHeightDp = configuration.screenHeightDp.dp
                    val initialValueDp = when (configuration.orientation) {
                        Configuration.ORIENTATION_PORTRAIT -> screenHeightDp / 2
                        else -> screenHeightDp
                    }
                    val initialValuePx = with(density) { initialValueDp.toPx() }
                    mutableIntStateOf(initialValuePx.toInt())
                }

                val expandedHeightDp = remember(expandedHeightPx) {
                    with(density) { expandedHeightPx.toDp() }
                }

                TopAppBar(
                    title = { },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                        scrolledContainerColor = Color.Transparent
                    ),
                    scrollBehavior = scrollBehavior,
                    expandedHeight = expandedHeightDp,
                    actions = {
                        val topMargin = remember {
                            val topMarginRatio = 0.035f
                            (topMarginRatio * configuration.screenHeightDp).dp
                        }

                        SubcomposeLayout { constraints ->
                            val contentMeasurable = subcompose("content") {
                                Column(
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(top = topMargin)
                                ) {
                                    IconButton(
                                        modifier = Modifier
                                            .padding(start = 8.dp),
                                        onClick = navigateUp,

                                        ) {
                                        Icon(
                                            modifier = Modifier
                                                .size(20.dp),
                                            imageVector = ImageVector
                                                .vectorResource(R.drawable.ic_arrow_left),
                                            contentDescription = stringResource(R.string.kembali),
                                            tint = Black10
                                        )
                                    }
                                    Spacer(Modifier.height(16.dp))

                                    Text(
                                        modifier = Modifier
                                            .padding(horizontal = 16.dp),
                                        text = stringResource(R.string.dunia_tanaman),
                                        style = MaterialTheme.typography.displaySmall,
                                        color = Green55
                                    )

                                    Spacer(Modifier.height(8.dp))

                                    Text(
                                        modifier = Modifier
                                            .padding(horizontal = 16.dp),
                                        text = stringResource(R.string.pelajari_dan_cari_cerita_terbaru_seputar_dunia_pertanian),
                                        style = MaterialTheme.typography.labelMedium,
                                        color = Grey60
                                    )

                                    Spacer(Modifier.height(28.dp))
                                }
                            }

                            val placeables = contentMeasurable.map {
                                it.measure(constraints)
                            }

                            val height = placeables.sumOf { it.height }
                            expandedHeightPx = height

                            layout(constraints.maxWidth, height) {
                                placeables.forEach {
                                    it.placeRelative(0, 0)
                                }
                            }
                        }
                    }
                )
            }
        ) { innerPadding ->


            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                NewsScreenBody(
                    listState = listState,
                    navigateToDetail = navigateToDetail
                )
            }
        }

        val coroutineScope = rememberCoroutineScope()
        ScrollUpButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .fillMaxHeight(0.15f)
                .fillMaxWidth(0.25f),
            lazyListStateProvider = { listState },
            onClick = {
                coroutineScope.launch {
                    listState.scrollToItem(0)
                    scrollBehavior.state.heightOffset = 0f
                }
            }
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NewsScreenBody(
    modifier: Modifier = Modifier,
    listState: LazyListState = rememberLazyListState(),
    navigateToDetail: () -> Unit = { },
) {
    var searchQuery by remember {
        mutableStateOf("")
    }

    val searchBarInteractionSource = remember {
        MutableInteractionSource()
    }

    val weeklyItemModifier = remember {
        Modifier
            .padding(start = 16.dp, end = 16.dp)
    }

    val newsItems = remember {
        getDummyWeeklyNewsItems()
    }

    var selectedNewsCategory by remember {
        mutableStateOf(NewsCategory.ALL)
    }

    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(bottom = 32.dp),
        state = listState,
    ) {

        stickyHeader {
            val listCategoryState = rememberLazyListState()
            SearchBar(
                modifier = Modifier
                    .background(Grey90)
                    .padding(top = 4.dp)
                    .padding(horizontal = 16.dp)
                    .wrapContentSize(),
                queryProvider = { searchQuery },
                onQueryChange = {
                    searchQuery = it
                },
                hint = stringResource(R.string.cari_segala_hal_terkait_dunia_perkebunan),
                interactionSource = searchBarInteractionSource
            )

            Spacer(
                Modifier
                    .fillMaxWidth()
                    .background(Grey90)
                    .height(12.dp)
            )

            LazyRow(
                state = listCategoryState,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Grey90),
                contentPadding = PaddingValues(start = 16.dp, bottom = 8.dp, end = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(NewsCategory.entries, key = { it.ordinal }) {
                    SearchCategory(
                        isSelected = it == selectedNewsCategory,
                        text = stringResource(it.textResId)
                    )
                }
            }
        }

        itemsIndexed(newsItems, key = { _, it -> it.id }) { index, item ->
            Spacer(
                Modifier.height(
                    if (index == 0) 8.dp else 16.dp
                )
            )

            WeeklyNews(
                modifier = weeklyItemModifier
                    .clickable(onClick = navigateToDetail),
                item = item
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F5F5)
@Composable
private fun NewsScreenPreview() {
    KakaoXpertTheme {
        NewsScreen()
    }
}