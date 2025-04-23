package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.takephoto.util

import android.content.Context
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.net.toUri
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.util.concurrent.Executors

@Stable
class CameraState(
    private val coroutineScope: CoroutineScope
) {
    var isUsingBackCamera by mutableStateOf(true)
        private set

    var isCameraOpen by mutableStateOf(true)
        private set

    val imageCapture = ImageCapture.Builder()
        .build()

    fun setToggleCameraLens() {
        isUsingBackCamera = !isUsingBackCamera
    }

    fun setIsCameraOpen(
        newIsCameraOpen: Boolean
    ) {
        isCameraOpen = newIsCameraOpen
    }

    private val executor = Executors.newSingleThreadExecutor()

    private val _captureImageResult = MutableSharedFlow<ImageCaptureResult>()
    val captureImageResult = _captureImageResult.asSharedFlow()

    fun captureImage(
        context: Context,
    ) {
        val outputFile = createCustomTempFile(context)
        val outputOptions = ImageCapture.OutputFileOptions
            .Builder(outputFile)
            .build()

        imageCapture.takePicture(
            outputOptions,
            executor,
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val imageUri = outputFileResults.savedUri ?: outputFile.toUri()
                    coroutineScope.launch {
                        _captureImageResult.emit(
                            ImageCaptureResult.Success(imageUri.toString())
                        )
                    }
                }

                override fun onError(exception: ImageCaptureException) {
                    coroutineScope.launch {
                        _captureImageResult.emit(
                            ImageCaptureResult.Error(exception)
                        )
                    }
                }
            }
        )
    }

    fun cleanResource() {
        executor.shutdown()
        coroutineScope.cancel()
    }
}

sealed interface ImageCaptureResult {
    data class Success(val imageUriPath: String) : ImageCaptureResult
    data class Error(val exception: Exception) : ImageCaptureResult
}
