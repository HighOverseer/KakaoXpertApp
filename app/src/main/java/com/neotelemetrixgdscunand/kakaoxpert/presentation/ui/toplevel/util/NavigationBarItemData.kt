package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.toplevel.util

import androidx.compose.runtime.Immutable
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.Navigation

@Immutable
data class NavigationBarItemData(
    val titleRestId: Int,
    val iconResId: Int,
    val route: Navigation.Main.MainRoute
)
