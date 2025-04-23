package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.takephoto.component

import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.takephoto.util.CameraState
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.takephoto.util.getCameraProvider

@Composable
fun CameraPreview(
    modifier: Modifier = Modifier,
    state: CameraState = CameraState(rememberCoroutineScope())
) {

    if (state.isCameraOpen) {
        val context = LocalContext.current
        val lifecycleOwner = LocalLifecycleOwner.current

        val lensFacing = remember(state.isUsingBackCamera) {
            if (state.isUsingBackCamera) {
                CameraSelector.LENS_FACING_BACK
            } else CameraSelector.LENS_FACING_FRONT
        }

        val preview = remember {
            Preview.Builder().build()
        }

        val previewView = remember {
            PreviewView(context)
        }

        val cameraxSelector = remember(lensFacing) {
            CameraSelector.Builder()
                .requireLensFacing(lensFacing)
                .build()
        }

        LaunchedEffect(lensFacing) {
            val cameraProvider = context.getCameraProvider()
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                lifecycleOwner,
                cameraxSelector,
                preview,
                state.imageCapture
            )
            preview.surfaceProvider = previewView.surfaceProvider
        }

        AndroidView(
            modifier = modifier,
            factory = { previewView },
        )
    } else {
        Box(
            modifier = modifier
                .background(color = Color.Black)
        )
    }

}

