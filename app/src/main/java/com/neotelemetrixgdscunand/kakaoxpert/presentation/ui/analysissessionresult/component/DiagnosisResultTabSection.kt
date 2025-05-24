package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.analysissessionresult.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.neotelemetrixgdscunand.kakaoxpert.R
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Green55
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Grey55
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Grey90
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.KakaoXpertTheme

@Composable
fun DiagnosisResultTabSection(
    modifier: Modifier = Modifier,
    isDiagnosisTabSelected: Boolean = true,
    changeSelectedTab: (Boolean) -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(color = Grey90)
            .padding(16.dp)
            .background(color = Color.White, shape = RoundedCornerShape(8.dp))
            .padding(horizontal = 4.dp)
    ) {
        Button(
            modifier = Modifier
                .weight(1f),
            shape = RoundedCornerShape(6.dp),
            onClick = {
                changeSelectedTab(true)
            },
            contentPadding = PaddingValues(vertical = 8.dp),
            colors = ButtonDefaults.elevatedButtonColors(
                containerColor = if (isDiagnosisTabSelected) Green55 else Color.Transparent
            )
        ) {
            Text(
                stringResource(R.string.info_penyakit),
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = if (isDiagnosisTabSelected) Color.White else Grey55
            )
        }

        Spacer(Modifier.width(24.dp))

        Button(
            modifier = Modifier
                .weight(1f),
            shape = RoundedCornerShape(6.dp),
            onClick = {
                changeSelectedTab(false)
            },
            contentPadding = PaddingValues(vertical = 8.dp),
            colors = ButtonDefaults.elevatedButtonColors(
                containerColor = if (!isDiagnosisTabSelected) Green55 else Color.Transparent
            )
        ) {
            Text(
                stringResource(R.string.harga_jual),
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = if (!isDiagnosisTabSelected) Color.White else Grey55
            )
        }
    }

}

@Preview
@Composable
private fun DiagnosisResultTabSectionPreview() {
    KakaoXpertTheme {
        DiagnosisResultTabSection()
    }
}