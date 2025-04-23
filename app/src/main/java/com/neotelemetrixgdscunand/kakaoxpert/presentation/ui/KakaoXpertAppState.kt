package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@Composable
fun rememberKakaoXpertAppState(
    rootNavHostController: NavHostController = rememberNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    windowInsetsController: WindowInsetsControllerCompat,
): KakaoXpertAppState {
    return remember(
        rootNavHostController,
        coroutineScope,
        windowInsetsController,
    ) {
        KakaoXpertAppState(
            rootNavHostController,
            coroutineScope,
            windowInsetsController,
        )
    }
}

@Stable
class KakaoXpertAppState(
    rootNavHostController: NavHostController,
    coroutineScope: CoroutineScope,
    private val windowInsetsController: WindowInsetsControllerCompat,
) {
    private var isCameraPermissionGranted by mutableStateOf<Boolean?>(null)
    val isCameraPermissionGrantedProvider = { isCameraPermissionGranted }

    private fun hideStatusBar() {
        windowInsetsController.hide(WindowInsetsCompat.Type.statusBars())
    }

    private fun showStatusBar() {
        windowInsetsController.show(WindowInsetsCompat.Type.statusBars())
    }

    @Composable
    fun rememberCameraPermissionRequest(): ManagedActivityResultLauncher<String, Boolean> =
        rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            isCameraPermissionGranted = isGranted
        }

    fun checkCameraPermission(
        context: Context,
        cameraPermissionRequest: ManagedActivityResultLauncher<String, Boolean>
    ) {
        val isAlreadyGranted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

        if (!isAlreadyGranted) {
            cameraPermissionRequest.launch(
                Manifest.permission.CAMERA
            )
        } else isCameraPermissionGranted = true
    }

    private val currentRouteStringVal: StateFlow<String?> =
        rootNavHostController.currentBackStackEntryFlow
            .map { value: NavBackStackEntry ->
                value.destination.route
            }
            .stateIn(
                coroutineScope,
                SharingStarted.WhileSubscribed(5000L),
                null
            )

//    private val currentRouteStringVal:StateFlow<Navigation.Route?> =
//        rootNavHostController.currentBackStackEntryFlow
//            .map { value: NavBackStackEntry ->
//                value.toRoute<Navigation.Route>()
//            }
//            .stateIn(
//                coroutineScope,
//                SharingStarted.WhileSubscribed(5000L),
//                null
//            )

    val shouldShowStatusBar: StateFlow<Boolean> =
        currentRouteStringVal
            .map { value ->
                value != Navigation.TakePhoto.stringVal
            }
            .distinctUntilChanged()
            .stateIn(
                coroutineScope,
                SharingStarted.WhileSubscribed(5000L),
                true
            )

    @Composable
    fun HandleStatusBarVisibilityEffect(shouldShowStatusBar: Boolean) {
        LaunchedEffect(shouldShowStatusBar) {
            if (shouldShowStatusBar) {
                showStatusBar()
            } else {
                hideStatusBar()
            }
        }
    }
}