package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.toplevel.iotdevicesinfo.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.neotelemetrixgdscunand.kakaoxpert.R
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Black10
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Green55
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.KakaoXpertTheme
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Red50
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.auth.component.PrimaryButton
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.takephoto.component.SecondaryButton
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.toplevel.iotdevicesinfo.IoTDeviceResetConfirmationDialogState

@Composable
fun IoTDeviceResetConfirmationDialog(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit = {},
    state: IoTDeviceResetConfirmationDialogState = IoTDeviceResetConfirmationDialogState(),
    onConfirm: (Int) -> Unit = {},
) {
    state.selectedIoTDeviceId?.let { iotDeviceId ->
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
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    Spacer(
                        Modifier
                            .height(24.dp)
                            .heightIn(min = 16.dp, max = 64.dp)
                    )

                    Text(
                        stringResource(R.string.apakah_anda_yakin_ingin_mereset_data_sensor_dari_perangkat_iot_ini),
                        textAlign = TextAlign.Center,
                        color = Black10,
                        style = MaterialTheme.typography.titleMedium.copy(
                            lineHeight = 21.sp
                        )
                    )

                    Spacer(
                        Modifier
                            .height(24.dp)
                    )

                    Row(
                        Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        PrimaryButton(
                            modifier = Modifier.weight(1f),
                            text = stringResource(R.string.batal),
                            onClick = onDismiss,
                            enabled = state.canInteract
                        )

                        Spacer(
                            Modifier
                                .width(8.dp)
                        )

                        SecondaryButton(
                            modifier = Modifier.weight(1f),
                            text = stringResource(R.string.iya),
                            textColor = Red50,
                            borderColor = Green55,
                            onClick = {
                                onConfirm(iotDeviceId)
                            },
                            enabled = state.canInteract
                        )
                    }


                    Spacer(
                        Modifier
                            .height(24.dp)
                            .heightIn(min = 16.dp, max = 64.dp)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun IoTDeviceResetConfirmationDialogPreview() {
    KakaoXpertTheme {
        IoTDeviceResetConfirmationDialog()
    }
}