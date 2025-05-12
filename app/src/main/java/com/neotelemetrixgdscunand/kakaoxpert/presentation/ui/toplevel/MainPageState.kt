package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.toplevel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.neotelemetrixgdscunand.kakaoxpert.R
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.Navigation
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.toplevel.util.NavigationBarItemData
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@Composable
fun rememberMainPageState(
    navHostController: NavHostController = rememberNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope()
): MainPageState {
    return remember(
        navHostController,
        coroutineScope
    ) {
        MainPageState(
            navHostController = navHostController,
            coroutineScope = coroutineScope
        )
    }
}

@Stable
class MainPageState(
    private val navHostController: NavHostController,
    coroutineScope: CoroutineScope,
) {

    val bottomNavigationBarItemData: ImmutableList<NavigationBarItemData> = getNavigationBarItems()

    private val topLevelRoutesStringValMap = mapOf(
        Navigation.Main.Home.stringVal to Navigation.Main.Home,
        Navigation.Main.Diagnosis.stringVal to Navigation.Main.Diagnosis,
        Navigation.Main.IoTDevicesInfo.stringVal to Navigation.Main.IoTDevicesInfo,
        Navigation.Main.Account.stringVal to Navigation.Main.Account
    )

    val currentSelectedTopLevelRoute: StateFlow<Navigation.Main.MainRoute?> =
        navHostController.currentBackStackEntryFlow
            .map {
                val currentRoute = it.destination.route
                topLevelRoutesStringValMap[currentRoute]
            }
            .distinctUntilChanged()
            .stateIn(
                coroutineScope,
                SharingStarted.WhileSubscribed(5000L),
                null
            )

    fun navigateToTopLevel(
        nextDestination: Navigation.Main.MainRoute
    ) {
        val currentSelectedTopLevelRoute = currentSelectedTopLevelRoute.value

        if (currentSelectedTopLevelRoute == nextDestination) return

        navHostController.navigate(
            nextDestination,
        ) {
            // To have a fresh top level destination instance when navigating in top level
            popUpTo(navHostController.graph.startDestinationId) {
                //inclusive = nextDestination == Navigation.Main.Home
                saveState = true

            }
            launchSingleTop = true
            restoreState = true
        }
    }


    private fun getNavigationBarItems(): ImmutableList<NavigationBarItemData> {
        return persistentListOf(
            NavigationBarItemData(
                titleRestId = R.string.beranda,
                iconResId = R.drawable.ic_home,
                route = Navigation.Main.Home
            ),
            NavigationBarItemData(
                titleRestId = R.string.analisis,
                iconResId = R.drawable.ic_camera,
                route = Navigation.Main.Diagnosis
            ),
            NavigationBarItemData(
                titleRestId = R.string.perangkat,
                iconResId = R.drawable.ic_connectivity,
                route = Navigation.Main.IoTDevicesInfo
            ),
            NavigationBarItemData(
                titleRestId = R.string.akun,
                iconResId = R.drawable.ic_person,
                route = Navigation.Main.Account
            ),
        )
    }
}