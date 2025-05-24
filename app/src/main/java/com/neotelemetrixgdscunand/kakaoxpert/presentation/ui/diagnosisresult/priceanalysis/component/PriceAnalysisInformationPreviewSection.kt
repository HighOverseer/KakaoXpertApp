package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.diagnosisresult.priceanalysis.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.neotelemetrixgdscunand.kakaoxpert.R
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Green55
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.KakaoXpertTheme
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Orange90
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Yellow90

@Composable
fun PriceAnalysisInformationPreviewSection(
    modifier: Modifier = Modifier,
    showSnackbar: (String) -> Unit = { }
) {
    Column(
        modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .background(color = Yellow90, shape = RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            Image(
                imageVector = ImageVector.vectorResource(R.drawable.ic_info),
                contentScale = ContentScale.Fit,
                contentDescription = "",
                modifier = Modifier.size(20.dp)
            )

            Spacer(Modifier.width(16.dp))

            Text(
                stringResource(R.string.price_note),
                style = MaterialTheme.typography.labelMedium,
                color = Green55,
                textAlign = TextAlign.Start
            )
        }

        Spacer(Modifier.height(24.dp))

        val featureNotYetAvailableMessage = stringResource(R.string.fitur_belum_tersedia)
        Text(
            modifier = Modifier
                .clickable(onClick = {
                    showSnackbar(featureNotYetAvailableMessage)
                }),
            text = stringResource(R.string.klik_untuk_prediksi_harga_dengan_porsi_yang_berbeda),
            style = MaterialTheme.typography.titleMedium.copy(
                textDecoration = TextDecoration.Underline,
                letterSpacing = (-0.175).sp
            ),
            color = Orange90,
            textAlign = TextAlign.Start
        )
    }
}

@Preview
@Composable
private fun PriceAnalysisInformationPreviewSectionPreview() {
    KakaoXpertTheme {
        PriceAnalysisInformationPreviewSection()
    }
}