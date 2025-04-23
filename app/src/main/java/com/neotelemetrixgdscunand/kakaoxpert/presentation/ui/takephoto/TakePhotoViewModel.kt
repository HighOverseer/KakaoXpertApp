package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.takephoto

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.asclepius.domain.common.StringRes
import com.neotelemetrixgdscunand.kakaoxpert.R
import com.neotelemetrixgdscunand.kakaoxpert.domain.presentation.CaptureImageFileHandler
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.takephoto.util.CameraState
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.takephoto.util.ImageCaptureResult
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.takephoto.util.TakePhotoUIEvent
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.takephoto.util.TextFieldConfirmationDialogEvent
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.takephoto.util.TextFieldConfirmationDialogState
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.util.ImageCompressor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TakePhotoViewModel @Inject constructor(
    private val imageCompressor: ImageCompressor,
    private val captureImageFileHandler: CaptureImageFileHandler
) : ViewModel() {

    val cameraState = CameraState(viewModelScope)
    val textFieldConfirmationDialogState = TextFieldConfirmationDialogState(viewModelScope)

    private var capturedPhotoImagePath: String? = null

    private val _uiEvent = Channel<TakePhotoUIEvent>()
    val uiEvent = _uiEvent
        .receiveAsFlow()

    init {
        listenToImageCaptureResult()
        listenToTextFieldConfirmationDialogEvent()
    }

    private var submitJob: Job? = null

    private fun listenToImageCaptureResult() {
        viewModelScope.launch {
            cameraState.captureImageResult.collect { result ->
                when (result) {
                    is ImageCaptureResult.Success -> {
                        capturedPhotoImagePath = result.imageUriPath
                        textFieldConfirmationDialogState.setIsShown(true)
                        cameraState.setIsCameraOpen(false)
                    }

                    is ImageCaptureResult.Error -> {
                        val message = StringRes.Dynamic(result.exception.message.toString())
                        _uiEvent.send(
                            TakePhotoUIEvent.OnToastMessage(message)
                        )
                    }
                }
            }
        }
    }

    private fun listenToTextFieldConfirmationDialogEvent() {
        viewModelScope.launch {
            textFieldConfirmationDialogState.event.collect { event ->
                when (event) {
                    is TextFieldConfirmationDialogEvent.OnSubmitted -> {
                        if (event.submittedText.isBlank()) {
                            val message =
                                StringRes.Static(R.string.nama_yang_dimasukkan_tidak_valid)
                            viewModelScope.launch {
                                _uiEvent.send(
                                    TakePhotoUIEvent.OnToastMessage(message)
                                )
                            }
                        } else {
                            if (submitJob?.isActive == true) return@collect

                            submitJob = viewModelScope.launch secondLaunch@{
                                textFieldConfirmationDialogState.setCanUserInteract(false)

                                val capturePhotoImagePath =
                                    capturedPhotoImagePath ?: return@secondLaunch
                                val compressedImage =
                                    imageCompressor.compressImage(capturePhotoImagePath)
                                val imagePath =
                                    captureImageFileHandler.saveImage(
                                        capturePhotoImagePath,
                                        compressedImage,
                                        event.submittedText
                                    )

                                if (imagePath == null) {
                                    capturedPhotoImagePath?.let {
                                        captureImageFileHandler.deleteImageFile(
                                            it
                                        )
                                    }
                                    capturedPhotoImagePath = null
                                    textFieldConfirmationDialogState.setIsShown(false)
                                    cameraState.setIsCameraOpen(true)
                                } else {
                                    textFieldConfirmationDialogState.setIsShown(false)
                                    _uiEvent.send(
                                        TakePhotoUIEvent.NavigateToResult(
                                            sessionName = event.submittedText,
                                            imagePath = imagePath
                                        )
                                    )
                                }
                            }.also {
                                it.invokeOnCompletion {
                                    textFieldConfirmationDialogState.setCanUserInteract(true)
                                    submitJob = null
                                }
                            }
                        }
                    }

                    TextFieldConfirmationDialogEvent.OnDismissed -> {
                        capturedPhotoImagePath?.let { captureImageFileHandler.deleteImageFile(it) }
                        capturedPhotoImagePath = null
                        textFieldConfirmationDialogState.setIsShown(false)
                        cameraState.setIsCameraOpen(true)
                    }
                }
            }
        }
    }


    fun handleOnPickImageGalleryResult(imageUriPath: String?) {
        capturedPhotoImagePath = imageUriPath
        if (imageUriPath != null) {
            textFieldConfirmationDialogState.setIsShown(true)
            cameraState.setIsCameraOpen(false)
        }
    }

    override fun onCleared() {
        cameraState.cleanResource()
        textFieldConfirmationDialogState.cleanResource()
        super.onCleared()
    }


}