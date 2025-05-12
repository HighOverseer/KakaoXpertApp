package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.diagnosisresult.priceanalysis.component

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.neotelemetrixgdscunand.kakaoxpert.R
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Black10
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Green55
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.KakaoXpertTheme
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Orange90
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.diagnosisresult.component.SecondaryDescription
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.util.ImagePainterStable
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.auth.component.PrimaryTextField

@Composable
fun PriceAnalysisOverview(modifier: Modifier = Modifier) {
    Column(
        modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .background(color = Color.White, shape = RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {
        SecondaryDescription(
            title = stringResource(R.string.total_buah_keseluruhan),
            description = "3 Buah"
        )

        Spacer(Modifier.height(16.dp))

        Text(
            stringResource(R.string.rata_rata_bobot_buah),
            style = MaterialTheme.typography.titleMedium,
            color = Black10
        )

        Spacer(Modifier.height(8.dp))

        val allowedCharacterPattern = remember {
            "^[0-9]*[,]{0,1}[0-9]*$".toRegex()
        }

        val interactionSource = remember {
            MutableInteractionSource()
        }

        var cacaoAverageWeightInput by remember { mutableStateOf("1") }
        val isFocused by interactionSource.collectIsFocusedAsState()

        PrimaryTextField(
            contentPadding = PaddingValues(vertical = 13.5.dp, horizontal = 16.dp),
            hintText = stringResource(R.string.masukkan_jumlah),
            valueProvider = { cacaoAverageWeightInput },
            onValueChange = {
                if (allowedCharacterPattern.matches(it)) {
                    cacaoAverageWeightInput = it
                }
            },
            textColor = Black10,
            isFocusedProvider = { isFocused },
            interactionSource = interactionSource,
            isBordered = true,
        )

        Spacer(Modifier.height(16.dp))

        Column(
            Modifier
                .fillMaxWidth()
                .background(color = Green55, shape = RoundedCornerShape(8.dp))
                .padding(vertical = 8.dp, horizontal = 16.dp)
        ) {
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ImagePainterStable(
                    drawableResId = R.drawable.ic_price,
                    contentScale = ContentScale.Fit,
                    contentDescription = null
                )

                Spacer(Modifier.width(8.dp))

                Text(
                    stringResource(R.string.prediksi_harga_jual),
                    style = MaterialTheme.typography.titleMedium,
                    color = Orange90
                )
            }

            Spacer(Modifier.height(16.dp))

            Text(
                "Rp20.000-30.000",
                style = MaterialTheme.typography.labelMedium,
                color = Color.White
            )
        }

        Spacer(Modifier.height(16.dp))

        Text(
            "*Cek rincian prediksi harga jual dibawah ini",
            style = MaterialTheme.typography.labelMedium,
            color = Black10
        )
    }
}

@Preview
@Composable
private fun PriceAnalysisOverviewPreview() {
    KakaoXpertTheme {
        PriceAnalysisOverview()
    }
}