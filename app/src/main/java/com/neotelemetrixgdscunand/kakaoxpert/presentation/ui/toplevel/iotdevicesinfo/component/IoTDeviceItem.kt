package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.toplevel.iotdevicesinfo.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.neotelemetrixgdscunand.kakaoxpert.R
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.IoTDevice
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Black10
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Green55
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Grey60
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Grey69
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.KakaoXpertTheme
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Orange85
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Red50

@Composable
fun IoTDeviceItem(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = { },
    onMoreMenuClicked: () -> Unit = { },
    ioTDevice: IoTDevice = IoTDevice(name = "Perangkat 0", id = 0, isActive = false)
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        onClick = onClick,
        shape = RoundedCornerShape(8.dp)
    ) {
        Row {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp),
                verticalAlignment = Alignment.Top
            ) {
                Icon(
                    modifier = Modifier
                        .padding(top = 2.dp),
                    imageVector = ImageVector.vectorResource(
                        R.drawable.ic_iot_device
                    ),
                    contentDescription = null,
                    tint = Green55
                )

                Spacer(Modifier.width(16.dp))

                Column {
                    Row(Modifier.fillMaxWidth()) {
                        Text(
                            modifier = Modifier.padding(),
                            text = ioTDevice.name,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.titleMedium,
                            color = Black10
                        )
                        Spacer(Modifier.width(8.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .background(
                                    if (ioTDevice.isActive) Green55 else Orange85,
                                    shape = RoundedCornerShape(4.dp)
                                )
                                .padding(horizontal = 4.dp, vertical = 2.dp)
                        ) {
                            Image(
                                modifier = Modifier.size(8.dp),
                                imageVector = ImageVector.vectorResource(if(ioTDevice.isActive) R.drawable.ic_checklist else R.drawable.ic_close),
                                contentDescription = null,
                                colorFilter = ColorFilter.tint(Color.White)
                            )
                            Spacer(Modifier.width(4.dp))
                            Text(
                                text = if(ioTDevice.isActive) stringResource(R.string.aktif) else stringResource(
                                    R.string.tidak_aktif
                                ),
                                color = Color.White,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }

                    Spacer(Modifier.height(6.dp))

                    Text(
                        text = ioTDevice.id.toString(),
                        style = MaterialTheme.typography.bodySmall,
                        color = Grey69
                    )

                    Spacer(Modifier.height(8.dp))
                }
            }

            IconButton(
                onClick = onMoreMenuClicked
            ) {
                Icon(
                    modifier = Modifier
                        .size(16.dp),
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = null,
                    tint = Grey60
                )
            }
        }

    }
}

@Preview
@Composable
private fun IoTDeviceItemPreview() {
    KakaoXpertTheme {
        IoTDeviceItem()
    }
}