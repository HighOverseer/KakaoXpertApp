package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.shop

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.neotelemetrixgdscunand.kakaoxpert.R
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.getShopItemDataDummies
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Black10
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Grey90
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.KakaoXpertTheme
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.toplevel.diagnosishistory.component.SearchBar
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.toplevel.diagnosishistory.component.SearchCategory
import com.neotelemetrixgdscunand.kamekapp.presentation.ui.news.NewsCategory


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShopScreen(
    modifier: Modifier = Modifier,
    navigateUp: () -> Unit = {},
) {

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val parentModifier = remember {
        modifier
            .fillMaxSize()
            .background(Color.White)
            .nestedScroll(scrollBehavior.nestedScrollConnection)
    }

    Scaffold(
        containerColor = Color.White,
        modifier = parentModifier,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors().copy(
                    scrolledContainerColor = Color.White,
                    containerColor = Color.White
                ),
                title = {},
                scrollBehavior = scrollBehavior,
                actions = {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .background(Color.White)
                    ) {
                        IconButton(
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .align(Alignment.CenterStart),
                            onClick = navigateUp
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
                            text = stringResource(R.string.toko),
                            style = MaterialTheme.typography.headlineSmall,
                            color = Black10
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
            ShopScreenBody()
        }

    }
}

@Composable
fun ShopScreenBody(modifier: Modifier = Modifier) {
    val selectedCategory by remember {
        mutableStateOf(
            NewsCategory.ALL
        )
    }

    val searchBarInteractionSource = remember {
        MutableInteractionSource()
    }

    var searchQuery by remember {
        mutableStateOf("")
    }

    Spacer(Modifier.height(8.dp))

    SearchBar(
        modifier = Modifier
            .padding(horizontal = 16.dp),
        queryProvider = { searchQuery },
        onQueryChange = {
            searchQuery = it
        },
        backgroundColor = Grey90,
        hint = stringResource(R.string.cari_segala_hal_terkait_dunia_perkebunan),
        interactionSource = searchBarInteractionSource,
    )

    Spacer(Modifier.height(8.dp))

    val listCategoryState = rememberLazyListState()
    LazyRow(
        state = listCategoryState,
        contentPadding = PaddingValues(start = 16.dp, bottom = 8.dp, end = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(NewsCategory.entries, key = { item -> item.ordinal }) { item ->
            SearchCategory(
                unselectedColor = Grey90,
                isSelected = item == selectedCategory,
                text = stringResource(item.textResId)
            )
        }
    }

    val lazyGridState = rememberLazyGridState()
    val shopItems = remember {
        getShopItemDataDummies()
    }
    LazyVerticalGrid(
        modifier = Modifier
            .background(color = Grey90),
        contentPadding = PaddingValues(
            top = 16.dp,
            bottom = 24.dp,
            start = 16.dp,
            end = 16.dp
        ),
        state = lazyGridState,
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        items(
            shopItems.size,
            key = { index ->
                shopItems[index].id
            }
        ) { index ->
            val currentShopItem = shopItems[index]
            ShopItem(
                imageUrl = currentShopItem.imageUrl,
                title = currentShopItem.title,
                price = currentShopItem.price,
                targetUrl = currentShopItem.targetUrl
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ShopScreenPreview() {
    KakaoXpertTheme {
        ShopScreen(Modifier)
    }
}