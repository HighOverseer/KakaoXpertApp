package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.toplevel.iotdevicesinfo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.neotelemetrixgdscunand.kakaoxpert.R
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Black10
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Grey90
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.KakaoXpertTheme
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Orange85
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.toplevel.IoTDataOverviewMenu
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.toplevel.iotdevicesinfo.component.AddIoTDeviceSection
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.toplevel.iotdevicesinfo.component.IoTDeviceItem
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.toplevel.iotdevicesinfo.component.SeeMoreMenu

@Composable
fun IoTDevicesInfoScreen(
    modifier: Modifier = Modifier,
    navigateToSensorDataDetails: () -> Unit = {}
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(color = Grey90)
            .padding(horizontal = 16.dp),
    ) {
        Spacer(Modifier.height(36.dp))

        Text(
            modifier = Modifier
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            text = stringResource(R.string.perangkat),
            style = MaterialTheme.typography.headlineSmall,
            color = Black10
        )

        Spacer(Modifier.height(42.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier
                    .width(16.dp)
                    .widthIn(min = 12.dp, max = 24.dp)
                    .aspectRatio(0.86f),
                imageVector = ImageVector
                    .vectorResource(R.drawable.ic_calendar),
                contentDescription = null,
                tint = Orange85
            )
            Spacer(Modifier.width(10.dp))
            Text(
                stringResource(R.string.overview_data_hari_ini),
                style = MaterialTheme.typography.headlineSmall,
                color = Black10
            )
        }

        Spacer(Modifier.height(16.dp))

        Row {
            IoTDataOverviewMenu(
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1.17f),
                iconResId = R.drawable.ic_thermometer,
                label = stringResource(R.string.suhu),
                value = "15-16°C",
            )
            Spacer(Modifier.width(16.dp))
            IoTDataOverviewMenu(
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1.17f),
                iconResId = R.drawable.ic_drop,
                label = stringResource(R.string.kelembapan),
                value = "96%",
            )
        }

        Spacer(Modifier.height(16.dp))

        Row {

            IoTDataOverviewMenu(
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1.17f),
                iconResId = R.drawable.ic_sun,
                label = stringResource(R.string.intensitas_cahaya),
                value = "100 Lux",
            )
            Spacer(Modifier.width(16.dp))

            SeeMoreMenu(
                Modifier
                    .weight(1f)
                    .aspectRatio(1.17f),
                onClick = navigateToSensorDataDetails
            )
        }

        Spacer(Modifier.height(40.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier
                    .width(21.dp)
                    .widthIn(min = 12.dp, max = 24.dp)
                    .aspectRatio(1.33f),
                imageVector = ImageVector
                    .vectorResource(R.drawable.ic_connectivity),
                contentDescription = null,
                tint = Orange85
            )
            Spacer(Modifier.width(10.dp))
            Text(
                stringResource(R.string.perangkat_tersambung),
                style = MaterialTheme.typography.headlineSmall,
                color = Black10
            )
        }

        Spacer(Modifier.height(16.dp))

        AddIoTDeviceSection(
            modifier = Modifier.fillMaxWidth()
        )

        repeat(2){
            Spacer(Modifier.height(16.dp))

            IoTDeviceItem()

        }

        Spacer(Modifier.height(32.dp))

    }


}

@Preview
@Composable
private fun IoTDevicesInfoScreenPreview() {
    KakaoXpertTheme {
        IoTDevicesInfoScreen()
    }
}