package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.cacaoimagedetail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.neotelemetrixgdscunand.kakaoxpert.R
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.cacaoimagedetail.components.OverlayCompose
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.diagnosisresult.component.NavigateUpButton
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.util.AsyncImagePainterStable
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.util.collectChannelWhenStarted

@Composable
fun CacaoImageDetailScreen(
    modifier: Modifier = Modifier,
    viewModel: CacaoImageDetailViewModel = hiltViewModel(),
    navigateUp: () -> Unit = { },
    showSnackbar: (String) -> Unit = { }
) {

    val lifecycleOwner = LocalLifecycleOwner.current
    val invalidSessionMessage = stringResource(R.string.invalid_session_please_try_again)
    LaunchedEffect(true) {
        lifecycleOwner.collectChannelWhenStarted(
            viewModel.invalidSession
        ) {
            showSnackbar(invalidSessionMessage)
        }
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    CacaoImageDetailContent(
        modifier = modifier,
        navigateUp = navigateUp,
        uiState = uiState
    )
}

@Composable
fun CacaoImageDetailContent(
    modifier: Modifier = Modifier,
    uiState: CocoaImageDetailUIState = CocoaImageDetailUIState(),
    navigateUp: () -> Unit = { },
) {
    val configuration = LocalConfiguration.current
    val topToArrowMargin = remember {
        val topToArrowMarginRatio = 0.04571f
        configuration.screenHeightDp.dp * topToArrowMarginRatio
    }

    Box(Modifier.fillMaxSize()) {
        AsyncImagePainterStable(
            modifier = modifier
                .fillMaxSize()
                .align(Alignment.Center),
            imageUrlOrPath = uiState.imagePath,
            placeholderResId = R.drawable.ic_camera,
            contentScale = ContentScale.FillBounds,
            contentDescription = null,
            alignment = Alignment.Center
        )

        OverlayCompose(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center),
            boundingBoxes = uiState.boundingBox
        )

        NavigateUpButton(
            modifier = Modifier
                .align(Alignment.TopStart),
            topToArrowMargin = topToArrowMargin,
            navigateUp = navigateUp
        )
    }
}