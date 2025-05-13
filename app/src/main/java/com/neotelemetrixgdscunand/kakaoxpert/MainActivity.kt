package com.neotelemetrixgdscunand.kakaoxpert

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.rememberCoroutineScope
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.compose.rememberNavController
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.KakaoXpertTheme
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.KakaoXpertApp
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.rememberKakaoXpertAppState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Suppress("unused")
    private val viewModel:MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        val windowInsetsController = WindowInsetsControllerCompat(window, window.decorView)

        enableEdgeToEdge()
        setContent {
            KakaoXpertTheme {
                val rootNavHostController = rememberNavController()
                val coroutineScope = rememberCoroutineScope()
                val appState = rememberKakaoXpertAppState(
                    rootNavHostController,
                    coroutineScope,
                    windowInsetsController,
                )

                KakaoXpertApp(
                    appState = appState,
                    rootNavHostController = rootNavHostController
                )
            }
        }
    }
}
