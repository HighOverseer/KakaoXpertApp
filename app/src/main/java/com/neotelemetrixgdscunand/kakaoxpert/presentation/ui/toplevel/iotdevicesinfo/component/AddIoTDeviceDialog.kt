package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.toplevel.iotdevicesinfo.component

import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.neotelemetrixgdscunand.kakaoxpert.R
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Black10
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Grey60
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Grey90
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.KakaoXpertTheme
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.takephoto.component.SecondaryButton
import com.neotelemetrixgdscunand.kamekapp.presentation.ui.auth.component.PrimaryButton
import com.neotelemetrixgdscunand.kamekapp.presentation.ui.auth.component.PrimaryTextField

@Composable
fun AddIoTDeviceDialog(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit = {},
    isShownProvider: () -> Boolean = { true },
    onSubmit: () -> Unit = { },
) {
    if (isShownProvider()) {
        var deviceNameText by rememberSaveable {
            mutableStateOf("")
        }

        var deviceIdText by rememberSaveable {
            mutableStateOf("")
        }

        var deviceKeyText by rememberSaveable {
            mutableStateOf("")
        }

        var isKeyVisible by rememberSaveable {
            mutableStateOf(false)
        }

        val deviceNameTextFieldInteractionSource = remember {
            MutableInteractionSource()
        }
        val isDeviceNameTextFieldFocused by deviceNameTextFieldInteractionSource.collectIsFocusedAsState()

        val deviceIdTextFieldInteractionSource = remember {
            MutableInteractionSource()
        }
        val isDeviceIdTextFieldFocused by deviceIdTextFieldInteractionSource.collectIsFocusedAsState()

        val deviceKeyTextFieldInteractionSource = remember {
            MutableInteractionSource()
        }
        val isDeviceKeyTextFieldFocused by deviceKeyTextFieldInteractionSource.collectIsFocusedAsState()

        Dialog(
            onDismissRequest = onDismiss
        ) {
            Card(
                modifier = modifier,
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
                        .padding(horizontal = 16.dp, vertical = 32.dp)
                ) {

                    PrimaryTextField(
                        contentPadding = PaddingValues(vertical = 13.5.dp, horizontal = 16.dp),
                        valueProvider = { deviceNameText },
                        hintText = stringResource(R.string.nama_perangkat_baru),
                        onValueChange = { deviceNameText = it },
                        textColor = Black10,
                        backgroundColor = Grey90,
                        isFocusedProvider = { isDeviceNameTextFieldFocused },
                        interactionSource = deviceNameTextFieldInteractionSource,
                        isBordered = false
                    )

                    Spacer(Modifier.height(24.dp))

                    PrimaryTextField(
                        contentPadding = PaddingValues(vertical = 13.5.dp, horizontal = 16.dp),
                        valueProvider = { deviceIdText },
                        hintText = stringResource(R.string.masukan_device_id_disini),
                        onValueChange = { deviceIdText = it },
                        textColor = Black10,
                        backgroundColor = Grey90,
                        isFocusedProvider = { isDeviceIdTextFieldFocused },
                        interactionSource = deviceIdTextFieldInteractionSource,
                        isBordered = false
                    )

                    Spacer(Modifier.height(24.dp))

                    PrimaryTextField(
                        contentPadding = PaddingValues(vertical = 13.5.dp, horizontal = 16.dp),
                        valueProvider = { deviceKeyText },
                        hintText = stringResource(R.string.masukan_device_key_disini),
                        onValueChange = { deviceKeyText = it },
                        textColor = Black10,
                        backgroundColor = Grey90,
                        isFocusedProvider = { isDeviceKeyTextFieldFocused },
                        interactionSource = deviceKeyTextFieldInteractionSource,
                        isBordered = false,
                        visualTransformation = if (isKeyVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            Icon(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable {
                                        isKeyVisible = !isKeyVisible
                                    },
                                imageVector = if (isKeyVisible)
                                    ImageVector
                                        .vectorResource(
                                            R.drawable.ic_eye,
                                        )
                                else ImageVector
                                    .vectorResource(R.drawable.ic_hide_eye),
                                tint = Grey60,
                                contentDescription = stringResource(R.string.visibilitas_kunci_perangkat)
                            )
                        }
                    )

                    Spacer(Modifier.height(24.dp))

                    Row {
                        SecondaryButton(
                            modifier = Modifier.weight(1f),
                            text = stringResource(R.string.batal),
                            contentPadding = PaddingValues(vertical = 14.dp),
                            onClick = onDismiss,
                        )

                        Spacer(Modifier.width(14.dp))


                        PrimaryButton(
                            modifier = Modifier.weight(1f),
                            text = stringResource(R.string.selesai),
                            contentPadding = PaddingValues(vertical = 14.dp),
                            onClick = onSubmit,
                        )

                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun AddIoTDeviceDialogPreview() {
    KakaoXpertTheme {
        AddIoTDeviceDialog()
    }
}