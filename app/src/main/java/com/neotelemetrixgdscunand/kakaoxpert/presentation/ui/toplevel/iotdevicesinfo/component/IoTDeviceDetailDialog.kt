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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.neotelemetrixgdscunand.kakaoxpert.R
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.IoTDevice
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Green55
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.KakaoXpertTheme
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Orange85
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Red50
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.auth.component.AuthTextField
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.auth.component.PrimaryButton
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.takephoto.component.SecondaryButton

@Composable
fun IoTDeviceDetailDialog(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit = {},
    ioTDevice: IoTDevice? = IoTDevice(1, "Perangkat 1", false),
    onReset: (Int) -> Unit = { },
    onDelete: (Int) -> Unit = { }
) {
    ioTDevice?.let {
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

                    AuthTextField(
                        title = stringResource(R.string.nama_perangkat),
                        valueProvider = { ioTDevice.name },
                        enabled = false
                    )

                    Spacer(Modifier.height(16.dp))

                    AuthTextField(
                        title = stringResource(R.string.id_perangkat),
                        valueProvider = { ioTDevice.id.toString() },
                        enabled = false
                    )

                    Spacer(Modifier.height(16.dp))

                    val active = stringResource(R.string.aktif)
                    val inactive = stringResource(R.string.tidak_aktif)
                    AuthTextField(
                        title = stringResource(R.string.status),
                        valueProvider = { if (ioTDevice.isActive) active else inactive },
                        enabled = false
                    )

                    Spacer(
                        Modifier
                            .height(24.dp)
                    )

                    Row(
                        Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        SecondaryButton(
                            modifier = Modifier.weight(1f),
                            text = stringResource(R.string.hapus),
                            textColor = Red50,
                            borderColor = Red50,
                            onClick = {
                                onDelete(ioTDevice.id)
                            }
                        )

                        Spacer(
                            Modifier
                                .width(8.dp)
                        )

                        SecondaryButton(
                            modifier = Modifier.weight(1f),
                            text = stringResource(R.string.reset),
                            textColor = Orange85,
                            borderColor = Green55,
                            onClick = {
                                onReset(ioTDevice.id)
                            }
                        )
                    }


                    Spacer(
                        Modifier
                            .height(8.dp)
                    )


                    PrimaryButton(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(R.string.selesai),
                        onClick = onDismiss
                    )

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
private fun IoTDeviceDetailDialogPreview() {
    KakaoXpertTheme {
        IoTDeviceDetailDialog()
    }
}