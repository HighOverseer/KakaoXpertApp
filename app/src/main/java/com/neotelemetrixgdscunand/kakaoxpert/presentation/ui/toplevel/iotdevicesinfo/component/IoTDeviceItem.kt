package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.toplevel.iotdevicesinfo.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.neotelemetrixgdscunand.kakaoxpert.R
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Black10
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Green55
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Green60
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Grey60
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Grey69
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.KakaoXpertTheme
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.toplevel.iotdevicesinfo.IoTDevicesInfoScreen

@Composable
fun IoTDeviceItem(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = { },
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

                Column{
                    Text(
                        modifier = Modifier.padding(),
                        text = "Perangkat 1",
                        style = MaterialTheme.typography.titleMedium,
                        color = Black10
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = "ABC12345DEF67890c3VwZXJfc2VjcmVfdG9rZW5fZm9yX3Byb2plY3Q=",
                        style = MaterialTheme.typography.bodySmall,
                        color = Grey69
                    )
                }
            }

            IconButton(
                onClick = { }
            ) {
                Icon(
                    modifier = Modifier
                        .width(6.dp)
                        .aspectRatio(0.576f),
                    imageVector = ImageVector
                        .vectorResource(R.drawable.ic_right_arrow),
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