package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.takephoto.component

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.neotelemetrixgdscunand.kakaoxpert.R
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Black10
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Grey90
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.KakaoXpertTheme
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.auth.component.PrimaryButton
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.auth.component.PrimaryTextField
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.takephoto.util.TextFieldConfirmationDialogState

@Composable
fun TextFieldConfirmationDialog(
    textProvider: () -> String = { "" },
    onValueNameChange: (String) -> Unit = {},
    hintText: String = "",
    state: TextFieldConfirmationDialogState = TextFieldConfirmationDialogState(
        rememberCoroutineScope()
    )
) {

    if (state.isShown) {
        val interactionSource = remember {
            MutableInteractionSource()
        }

        val isFocused by interactionSource.collectIsFocusedAsState()

        Dialog(
            onDismissRequest = state::dismiss
        ) {
            Card(
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 4.dp
                ),
            ) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 40.dp)
                ) {

                    PrimaryTextField(
                        contentPadding = PaddingValues(vertical = 13.5.dp, horizontal = 16.dp),
                        valueProvider = textProvider,
                        hintText = hintText,
                        onValueChange = onValueNameChange,
                        textColor = Black10,
                        backgroundColor = Grey90,
                        enabled = state.canUserInteractWithDialog,
                        isFocusedProvider = { isFocused },
                        interactionSource = interactionSource,
                        isBordered = false
                    )

                    Spacer(Modifier.height(24.dp))

                    Row {
                        SecondaryButton(
                            modifier = Modifier.weight(1f),
                            text = stringResource(R.string.batal),
                            contentPadding = PaddingValues(vertical = 14.dp),
                            onClick = state::dismiss,
                            enabled = state.canUserInteractWithDialog
                        )

                        Spacer(Modifier.width(14.dp))


                        PrimaryButton(
                            modifier = Modifier.weight(1f),
                            text = stringResource(R.string.selesai),
                            contentPadding = PaddingValues(vertical = 14.dp),
                            onClick = state::submit,
                            enabled = state.canUserInteractWithDialog
                        )

                    }
                }
            }
        }
    }

}

@Preview
@Composable
private fun InputSessionNameDialogPreview() {
    KakaoXpertTheme {
        val coroutineScope = rememberCoroutineScope()
        val textFieldConfirmationDialogState = remember {
            TextFieldConfirmationDialogState(coroutineScope).apply {
                setIsShown(true)
            }
        }
        TextFieldConfirmationDialog(
            state = textFieldConfirmationDialogState
        )
    }
}