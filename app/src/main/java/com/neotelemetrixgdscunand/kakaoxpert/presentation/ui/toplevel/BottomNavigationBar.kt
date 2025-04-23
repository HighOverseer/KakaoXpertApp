package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.toplevel

import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Grey65
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.KakaoXpertTheme
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Green55
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Shadow
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.Navigation
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.toplevel.util.NavigationBarItemData
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun BottomNavigationBar(
    modifier: Modifier = Modifier,
    navigationBarItemsData: ImmutableList<NavigationBarItemData> = persistentListOf(),
    onSelectedNavigation: (Navigation.Main.MainRoute) -> Unit = {},
    selectedNavigationProvider: @Composable () -> Navigation.Main.MainRoute? = { null },
) {
    val selectedNavigation = selectedNavigationProvider()
    val isVisible = selectedNavigation != null
    if (isVisible) {
        BottomAppBar(
            modifier = modifier
                .shadow(
                    elevation = 16.dp,
                    spotColor = Shadow
                ),
            containerColor = Color.White,
        ) {
            navigationBarItemsData.forEach {
                val isSelected = selectedNavigation == it.route

                NavigationBarItem(
                    colors = NavigationBarItemDefaults.colors(
                        unselectedIconColor = Grey65,
                        selectedIconColor = Green55,
                        unselectedTextColor = Grey65,
                        selectedTextColor = Green55,
                        indicatorColor = Color.White
                    ),

                    selected = isSelected,
                    icon = {
                        Icon(
                            modifier = Modifier.size(27.dp),
                            imageVector = ImageVector.vectorResource(
                                id = it.iconResId
                            ),
                            contentDescription = stringResource(it.titleRestId)
                        )
                    },
                    onClick = {
                        onSelectedNavigation(it.route)
                    },
                    label = {
                        Text(
                            text = stringResource(it.titleRestId),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun MeasuredBottomNavigationBar(
    modifier: Modifier = Modifier,
    navigationBarItemsData: ImmutableList<NavigationBarItemData> = persistentListOf(),
    onSelectedNavigation: (Navigation.Main.MainRoute) -> Unit = {},
    selectedNavigationProvider: @Composable () -> Navigation.Main.MainRoute? = { null },
    onBottomNavigationBarHeightMeasured: (Int) -> Unit = {},
) {
    SubcomposeLayout { constraints ->
        val contentMeasurable = subcompose("bottombar") {
            BottomNavigationBar(
                modifier = modifier,
                navigationBarItemsData = navigationBarItemsData,
                onSelectedNavigation = onSelectedNavigation,
                selectedNavigationProvider = selectedNavigationProvider
            )
        }
        val placeables = contentMeasurable.map {
            it.measure(constraints)
        }

        val height = placeables.sumOf { it.height }
        onBottomNavigationBarHeightMeasured(height)

        layout(constraints.maxWidth, height) {
            placeables.forEach {
                it.placeRelative(0, 0)
            }
        }
    }
}

@Preview
@Composable
private fun BottomNavigationBarPreview() {
    KakaoXpertTheme {
        BottomNavigationBar(
            navigationBarItemsData = rememberMainPageState()
                .bottomNavigationBarItemData
        )
    }
}