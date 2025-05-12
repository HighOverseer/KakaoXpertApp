package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.toplevel.home.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.neotelemetrixgdscunand.kakaoxpert.presentation.dui.NewsItemDui
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun WeeklyNewsPreviewSection(
    modifier: Modifier = Modifier,
    newsItems: ImmutableList<NewsItemDui> = persistentListOf(),
    isLoadingNewsItemsPreviewProvider: () -> Boolean,
    onItemClicked: (Int) -> Unit = {}
) {
    if (isLoadingNewsItemsPreviewProvider()) {
        repeat(1) {
            key(it) {
                WeeklyNewsLoading(modifier)
            }
        }
    } else {
        newsItems.forEach { item ->
            WeeklyNews(
                modifier = modifier,
                item = item,
                onClick = {
                    onItemClicked(item.id)
                }
            )
        }
    }

    Spacer(Modifier.height(32.dp))
}