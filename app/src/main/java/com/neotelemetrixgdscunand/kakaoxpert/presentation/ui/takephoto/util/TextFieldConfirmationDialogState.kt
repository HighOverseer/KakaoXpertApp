package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.takephoto.util

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

@Stable
class TextFieldConfirmationDialogState(
    private val coroutineScope: CoroutineScope
) {
    var confirmationText by mutableStateOf("")
        private set

    var isShown by mutableStateOf(false)
        private set

    var canUserInteractWithDialog by mutableStateOf(true)
        private set

    private val _event = MutableSharedFlow<TextFieldConfirmationDialogEvent>()
    val event = _event.asSharedFlow()

    fun setIsShown(
        isShown: Boolean
    ) {
        this.isShown = isShown
    }

    fun setCanUserInteract(
        canUserInteract: Boolean
    ) {
        this.canUserInteractWithDialog = canUserInteract
    }

    fun submit() {
        if (canUserInteractWithDialog) {
            val submittedText = confirmationText
            coroutineScope.launch {
                _event.emit(
                    TextFieldConfirmationDialogEvent.OnSubmitted(submittedText)
                )
            }
        }
    }

    fun dismiss() {
        if (canUserInteractWithDialog) {
            confirmationText = ""
            coroutineScope.launch {
                _event.emit(
                    TextFieldConfirmationDialogEvent.OnDismissed
                )
            }
        }
    }

    fun setText(newText: String) {
        confirmationText = newText
    }

    fun cleanResource() {
        coroutineScope.cancel()
    }
}

interface TextFieldConfirmationDialogEvent {
    data class OnSubmitted(val submittedText: String) : TextFieldConfirmationDialogEvent
    data object OnDismissed : TextFieldConfirmationDialogEvent
}