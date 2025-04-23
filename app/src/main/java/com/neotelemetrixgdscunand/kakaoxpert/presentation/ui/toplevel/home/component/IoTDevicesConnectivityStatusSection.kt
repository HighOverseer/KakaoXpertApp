package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.toplevel.home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.traceEventEnd
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.neotelemetrixgdscunand.kakaoxpert.R
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Green55
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.KakaoXpertTheme
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Orange85
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Red
import kotlinx.coroutines.delay

val mapConnectivityToIconResId = hashMapOf(
    true to R.drawable.ic_checklist,
    false to R.drawable.ic_not_checklist
)
val mapConnectivityToTextInfoResId:HashMap<Boolean?, Int> = hashMapOf(
    true to R.string.telah_terhubung_dengan_perangkat_iot,
    false to R.string.belum_terhubung_dengan_perangkat_iot,
    null to R.string.sedang_menghubungkan_dengan_perangkat_iot
)

val mapConnectivityToTextColor = mapOf(
    true to Green55,
    false to Red,
    null to Orange85
)

@Composable
fun IoTDevicesConnectivityStatusSection(
    modifier: Modifier = Modifier,
    isConnected:Boolean? = null
) {
    Row(
        modifier
            .background(color = Color.White, shape = RoundedCornerShape(4.dp))
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        mapConnectivityToIconResId[isConnected]?.let { iconResId ->
            Image(
                imageVector = ImageVector
                    .vectorResource(iconResId),
                contentDescription = null
            )
        } ?: CircularProgressIndicator(
            Modifier.size(18.dp),
            color = Orange85,
            strokeWidth = 3.dp
        )


        Spacer(Modifier.width(8.dp))
        val textInfoResId = mapConnectivityToTextInfoResId[isConnected]
        Text(
            text = stringResource(textInfoResId ?: R.string.sedang_menghubungkan_dengan_perangkat_iot),
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 14.sp
            ),
            color = mapConnectivityToTextColor[isConnected] ?: Orange85
        )
    }



}

@Preview
@Composable
private fun IoTDevicesConnectivityStatusSectionPreview() {
    KakaoXpertTheme {
        var isConnected by remember { mutableStateOf<Boolean?>(false) }

        LaunchedEffect(true) {
            delay(2000L)
            isConnected = null
            delay(2000L)
            isConnected = true
        }

        IoTDevicesConnectivityStatusSection(isConnected = isConnected)
    }
}