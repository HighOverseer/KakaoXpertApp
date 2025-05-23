package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.neotelemetrixgdscunand.kakaoxpert.R
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Green55
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.KakaoXpertTheme
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Orange85
import com.neotelemetrixgdscunand.kakaoxpert.presentation.utils.collectChannelWhenStarted
import kotlinx.coroutines.delay


@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
    navigateToAuthPage: () -> Unit = {},
    navigateToMainPage: () -> Unit = {},
    navigateToOnBoarding: () -> Unit = {},
    viewModel: SplashViewModel = hiltViewModel()
) {

    val lifecycle = LocalLifecycleOwner.current
    LaunchedEffect(true) {
        delay(2000L)

        lifecycle.collectChannelWhenStarted(viewModel.isReadyEvent) {
            val (isAlreadyLoggedIn, isFirstTime) = it
            if (!isAlreadyLoggedIn) {
                navigateToAuthPage()
            } else {
                if (isFirstTime) {
                    navigateToOnBoarding()
                } else navigateToMainPage()
            }
        }
    }

    SplashContent(
        modifier = modifier
    )
}

@Composable
fun SplashContent(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier
                .size(147.dp)
                .sizeIn(
                    minWidth = 100.dp,
                    maxWidth = 200.dp,
                    minHeight = 100.dp,
                    maxHeight = 100.dp
                )
                .aspectRatio(1f),
            imageVector = ImageVector
                .vectorResource(R.drawable.ic_logo),
            contentDescription = stringResource(R.string.logo)
        )
        Row {
            Text(
                text = stringResource(R.string.kakao_),
                style = MaterialTheme.typography.displayLarge,
                color = Green55
            )
            Text(
                text = stringResource(R.string.xpert),
                style = MaterialTheme.typography.displayLarge,
                color = Orange85
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
private fun SplashContentPreview() {
    KakaoXpertTheme {
        SplashContent()
    }
}


