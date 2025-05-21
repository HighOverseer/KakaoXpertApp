package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.toplevel.home.component

import android.content.res.Configuration
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.neotelemetrixgdscunand.kakaoxpert.R
import com.neotelemetrixgdscunand.kakaoxpert.presentation.dui.IoTDataOverviewDui
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Black10
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.KakaoXpertTheme
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Orange85
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.toplevel.IoTDataOverviewMenu

@Composable
fun IoTDataOverviewSection(
    modifier: Modifier = Modifier,
    navigateToNews: () -> Unit = {},
    navigateToShop: () -> Unit = {},
    navigateToWeather: () -> Unit = {},
    ioTDataOverviewDui: IoTDataOverviewDui = IoTDataOverviewDui()
) {

    Column(
        modifier,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp),
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
            Spacer(Modifier.width(8.dp))
            Text(
                stringResource(R.string.overview_data_hari_ini),
                style = MaterialTheme.typography.headlineSmall,
                color = Black10
            )
        }

        Spacer(Modifier.height(16.dp))

        val configuration = LocalConfiguration.current
        val horizontalArrangement = remember {
            if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                Arrangement.SpaceBetween
            } else Arrangement.SpaceAround
        }

        val minWidth = configuration.screenWidthDp.dp / 2.25f

        val scrollState = rememberScrollState()
        Row(
            Modifier
                .fillMaxWidth()
                .horizontalScroll(scrollState),
            horizontalArrangement = horizontalArrangement
        ) {
            Spacer(Modifier.width(16.dp))

            IoTDataOverviewMenu(
                modifier = Modifier
                    .width(minWidth)
                    .widthIn(min = minWidth)
                    .aspectRatio(1.24f),
                iconResId = R.drawable.ic_thermometer,
                label = stringResource(R.string.suhu),
                value = ioTDataOverviewDui.averageTemperatureValue.getValue(),
                onClick = navigateToWeather
            )
            Spacer(Modifier.width(12.dp))
            IoTDataOverviewMenu(
                modifier = Modifier
                    .width(minWidth)
                    .widthIn(min = minWidth)
                    .aspectRatio(1.24f),
                iconResId = R.drawable.ic_drop,
                label = stringResource(R.string.kelembapan),
                value = ioTDataOverviewDui.averageHumidityValue.getValue(),
                onClick = navigateToNews
            )
            Spacer(Modifier.width(12.dp))
            IoTDataOverviewMenu(
                modifier = Modifier
                    .width(minWidth)
                    .widthIn(min = minWidth)
                    .aspectRatio(1.24f),
                iconResId = R.drawable.ic_sun,
                label = stringResource(R.string.intensitas_cahaya),
                value = ioTDataOverviewDui.averageLightIntensityValue.getValue(),
                onClick = navigateToShop
            )
            Spacer(Modifier.width(16.dp))
        }
    }

}

@Preview(showBackground = true)
@Composable
private fun IoTDataOverviewSectionPreview() {
    KakaoXpertTheme {
        IoTDataOverviewSection()
    }
}