package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui

import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Grey90
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.auth.LoginScreen
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.auth.RegisterScreen
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.cacaoimagedetail.CacaoImageDetailScreen
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.diagnosisresult.DiagnosisResultScreen
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.news.NewsDetailScreen
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.news.NewsScreen
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.notif.screen.CacaoRequestScreen
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.notif.screen.NotificationScreen
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.onboarding.OnBoardingScreen
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.profile.ProfileScreen
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.sensordatadetails.SensorDataDetailScreen
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.shop.ShopScreen
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.takephoto.TakePhotoScreen
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.toplevel.MainPage
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.toplevel.rememberMainPageState
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.weather.WeatherScreen
import kotlinx.coroutines.launch

@Composable
fun KakaoXpertApp(
    modifier: Modifier = Modifier,
    rootNavHostController: NavHostController = rememberNavController(),
    appState: KakaoXpertAppState,
) {
    val snackbarHostState = remember {
        SnackbarHostState()
    }
    val coroutineScope = rememberCoroutineScope()
    val showSnackbar: (String) -> Unit = remember {
        { message: String ->
            coroutineScope.launch {
                snackbarHostState.showSnackbar(message)
            }
        }
    }

    val shouldShowStatusBar by appState.shouldShowStatusBar.collectAsStateWithLifecycle()
    appState.HandleStatusBarVisibilityEffect(shouldShowStatusBar)

    Scaffold(
        containerColor = Grey90,
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets.exclude(
            NavigationBarDefaults.windowInsets
        ),
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                snackbar = { data ->
                    Text(data.visuals.message)
                }
            )
        }
    ) { innerPadding ->
        NavHost(
            modifier = modifier
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding),
            navController = rootNavHostController,
            startDestination = Navigation.Splash,
        ) {
            composable<Navigation.Splash> {
                SplashScreen(
                    navigateToAuthPage = {
                        rootNavHostController.navigate(Navigation.Auth) {
                            popUpTo<Navigation.Splash> {
                                inclusive = true
                            }
                        }
                    }
                )
            }

            composable<Navigation.OnBoarding> {
                OnBoardingScreen(
                    navigateUp = rootNavHostController::navigateUp,
                    navigateToMainPage = {
                        rootNavHostController.navigate(Navigation.Main) {
                            popUpTo<Navigation.OnBoarding> {
                                inclusive = true
                            }
                        }
                    }
                )
            }

            navigation<Navigation.Auth>(
                startDestination = Navigation.Auth.Login
            ) {
                composable<Navigation.Auth.Login> {
                    LoginScreen(
                        navigateToOnBoarding = {
                            rootNavHostController.navigate(Navigation.OnBoarding) {
                                popUpTo<Navigation.Auth> {
                                    inclusive = true
                                }
                            }
                        }
                    )
                }
                composable<Navigation.Auth.Register> {
                    RegisterScreen()
                }
            }

            composable<Navigation.Main> {
                val mainNavController = rememberNavController()
                val mainPageCoroutineScope = rememberCoroutineScope()

                val state = rememberMainPageState(
                    navHostController = mainNavController,
                    coroutineScope = mainPageCoroutineScope,
                )

                MainPage(
                    state = state,
                    mainNavHostController = mainNavController,
                    navigateToNews = {
                        rootNavHostController.navigate(
                            Navigation.News
                        )
                    },
                    navigateToShop = {
                        rootNavHostController.navigate(
                            Navigation.Shop
                        )
                    },
                    navigateToWeather = {
                        rootNavHostController.navigate(
                            Navigation.Weather
                        )
                    },
                    navigateToNewsDetail = {
                        rootNavHostController.navigate(
                            Navigation.NewsDetail
                        )
                    },
                    navigateToDiagnosisResult = { sessionId ->
                        rootNavHostController.navigate(
                            Navigation.DiagnosisResult(
                                sessionId = sessionId,
                                newSessionName = null,
                                newUnsavedSessionImagePath = null
                            )
                        )
                    },
                    navigateToNotification = {
                        rootNavHostController.navigate(
                            Navigation.Notification
                        )
                    },
                    navigateToTakePhoto = {
                        rootNavHostController.navigate(
                            Navigation.TakePhoto
                        )
                    },
                    navigateToProfile = {
                        rootNavHostController.navigate(
                            Navigation.Profile
                        )
                    },
                    navigateToSensorDataDetails = {
                        rootNavHostController.navigate(
                            Navigation.SensorDataDetails
                        )
                    }
                )
            }


            composable<Navigation.TakePhoto> {
                TakePhotoScreen(
                    isCameraPermissionGrantedProvider = appState.isCameraPermissionGrantedProvider,
                    showSnackBar = showSnackbar,
                    navigateUp = rootNavHostController::navigateUp,
                    navigateToResult = { newSessionName, newUnsavedSessionImagePath ->
                        rootNavHostController.navigate(
                            Navigation.DiagnosisResult(
                                sessionId = null,
                                newSessionName = newSessionName,
                                newUnsavedSessionImagePath = newUnsavedSessionImagePath
                            )
                        ) {
                            popUpTo<Navigation.TakePhoto> {
                                inclusive = true
                            }
                        }
                    },
                    checkCameraPermission = appState::checkCameraPermission,
                    rememberCameraPermissionRequest = { appState.rememberCameraPermissionRequest() }
                )
            }

            composable<Navigation.DiagnosisResult> {
                DiagnosisResultScreen(
                    navigateUp = rootNavHostController::navigateUp,
                    showSnackbar = showSnackbar,
                    navigateToCacaoImageDetail = { sessionId, detectedCacaoId, imagePreviewPath ->
                        rootNavHostController.navigate(
                            Navigation.CacaoImageDetail(
                                diagnosisSessionId = sessionId,
                                detectedCacaoId = detectedCacaoId,
                                imagePath = imagePreviewPath
                            )
                        )
                    }
                )
            }

            composable<Navigation.News> {
                NewsScreen(
                    navigateUp = rootNavHostController::navigateUp,
                    navigateToDetail = {
                        rootNavHostController.navigate(Navigation.NewsDetail)
                    }
                )
            }

            composable<Navigation.NewsDetail> {
                NewsDetailScreen(
                    navigateUp = rootNavHostController::navigateUp
                )
            }

            composable<Navigation.Weather> {
                WeatherScreen(
                    navigateUp = rootNavHostController::navigateUp
                )
            }

            composable<Navigation.Shop> {
                ShopScreen(
                    navigateUp = rootNavHostController::navigateUp
                )
            }

            composable<Navigation.Notification> {
                NotificationScreen(
                    navigateUp = rootNavHostController::navigateUp,
                    navigateToCacaoRequestScreen = {
                        rootNavHostController.navigate(
                            Navigation.CacaoRequest
                        )
                    }
                )
            }

            composable<Navigation.CacaoRequest> {
                CacaoRequestScreen(
                    navigateUp = rootNavHostController::navigateUp
                )
            }

            composable<Navigation.CacaoImageDetail> {
                CacaoImageDetailScreen(
                    navigateUp = rootNavHostController::navigateUp,
                    showSnackbar = showSnackbar
                )
            }

            composable<Navigation.Profile> {
                ProfileScreen(
                    navigateUp = rootNavHostController::navigateUp
                )
            }

            composable<Navigation.SensorDataDetails> {
                SensorDataDetailScreen(
                    navigateUp = rootNavHostController::navigateUp
                )
            }
        }
    }


}
