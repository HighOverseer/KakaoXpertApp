package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui

import android.Manifest
import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.neotelemetrixgdscunand.kakaoxpert.R
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
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

    private var isLocationPermissionGranted by mutableStateOf<Boolean?>(null)
    val isLocationPermissionGrantedProvider = { isLocationPermissionGranted }

    private var isNotificationPermissionGranted by mutableStateOf<Boolean?>(null)
    val isNotificationPermissionGrantedProvider = { isNotificationPermissionGranted }

    private fun hideStatusBar() {
        windowInsetsController.hide(WindowInsetsCompat.Type.statusBars())
    }

    private fun showStatusBar() {
        windowInsetsController.show(WindowInsetsCompat.Type.statusBars())
    }

    @Composable
    fun rememberCameraPermissionRequest(): ManagedActivityResultLauncher<Array<String>, Map<String, Boolean>> {
        return rememberPermissionRequest(
            onResult = { isGranted ->
                isCameraPermissionGranted = isGranted
            }
        )
    }

    @Composable
    fun rememberLocationPermissionRequest(): ManagedActivityResultLauncher<Array<String>, Map<String, Boolean>> {
        return rememberPermissionRequest(
            onResult = { isGranted ->
                isLocationPermissionGranted = isGranted
            }
        )
    }

    @Composable
    fun rememberNotificationPermissionRequest(modifier: Modifier = Modifier):ManagedActivityResultLauncher<Array<String>, Map<String, Boolean>> {
        return rememberPermissionRequest(
            onResult = {
                isNotificationPermissionGranted = it
            }
        )
    }

    @Composable
    fun rememberLocationSettingResolutionLauncher(
        context: Context,
        showSnackbar: (String) -> Unit,
        locationPermissionRequest: ManagedActivityResultLauncher<Array<String>, Map<String, Boolean>>
    ): ManagedActivityResultLauncher<IntentSenderRequest, ActivityResult> {
        return rememberLauncherForActivityResult(
            ActivityResultContracts.StartIntentSenderForResult()
        ) { result ->
            when (result.resultCode) {
                RESULT_OK -> {
                    checkLocationPermission(
                        context = context,
                        locationPermissionRequest
                    )
                }

                RESULT_CANCELED -> {
                    showSnackbar(context.getString(R.string.maaf_pengaturan_lokasi_perlu_anda_aktifkan))
                }
            }
        }
    }

    @Composable
    private fun rememberPermissionRequest(
        onResult: (Boolean) -> Unit
    ): ManagedActivityResultLauncher<Array<String>, Map<String, Boolean>> {
        return rememberLauncherForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { mapResult ->
            val isGranted = mapResult.values.all { it }
            onResult(isGranted)
        }
    }

    fun checkCameraPermission(
        context: Context,
        cameraPermissionRequest: ManagedActivityResultLauncher<Array<String>, Map<String, Boolean>>
    ) {
        checkPermission(
            context = context,
            permissionRequest = cameraPermissionRequest,
            permissions = persistentListOf(Manifest.permission.CAMERA),
            onAlreadyGranted = {
                isCameraPermissionGranted = true
            }
        )
    }

    fun checkLocationPermission(
        context: Context,
        locationPermissionRequest: ManagedActivityResultLauncher<Array<String>, Map<String, Boolean>>
    ) {
        checkPermission(
            context = context,
            permissionRequest = locationPermissionRequest,
            permissions = persistentListOf(Manifest.permission.ACCESS_COARSE_LOCATION),
            onAlreadyGranted = {
                isLocationPermissionGranted = true
            }
        )
    }


    fun checkNotificationPermission(
        context: Context,
        notificationPermissionRequest: ManagedActivityResultLauncher<Array<String>, Map<String, Boolean>>
    ){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            checkPermission(
                context = context,
                permissionRequest = notificationPermissionRequest,
                permissions = persistentListOf(Manifest.permission.POST_NOTIFICATIONS),
                onAlreadyGranted = {
                    isNotificationPermissionGranted = true
                }
            )
        }else isNotificationPermissionGranted = true
    }

    private fun isPermissionGranted(context: Context, permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun checkPermission(
        context: Context,
        permissions: ImmutableList<String>,
        permissionRequest: ManagedActivityResultLauncher<Array<String>, Map<String, Boolean>>,
        onAlreadyGranted: () -> Unit
    ) {
        val permissionsResults = permissions.map { isPermissionGranted(context, it) }
        val isAlreadyGranted = permissionsResults.all { it }

        if (!isAlreadyGranted) {
            permissionRequest.launch(permissions.toTypedArray())
        } else onAlreadyGranted()
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