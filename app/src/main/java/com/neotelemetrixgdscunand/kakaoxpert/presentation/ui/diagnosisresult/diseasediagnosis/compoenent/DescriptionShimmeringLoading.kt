package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.diagnosisresult.diseasediagnosis.compoenent

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.KakaoXpertTheme
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.diagnosisresult.util.shimmeringEffect


@Composable
fun DescriptionShimmeringLoading(
    modifier: Modifier = Modifier,
    lineHeight: Dp = 17.dp,
    lastLineWidthRatio: Float = 0.7f,
    lineCount: Int = 2
) {

    val boxModifier = remember {
        modifier
            .fillMaxWidth(1f)
            .height(lineHeight)
            .shimmeringEffect()
    }

    val lastBoxModifier = remember {
        modifier
            .fillMaxWidth(lastLineWidthRatio)
            .height(lineHeight)
            .shimmeringEffect()
    }

    repeat(lineCount) {
        val currentModifier = if (it == lineCount - 1) lastBoxModifier else boxModifier

        Box(
            modifier = currentModifier
        )

        if (it != lineCount - 1) {
            Spacer(Modifier.height(4.dp))
        }
    }
}

@Preview
@Composable
private fun DescriptionShimmeringLoadingPreview() {
    KakaoXpertTheme {
        DescriptionShimmeringLoading()
    }
}