package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.analysissessionresult.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.KakaoXpertTheme
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.analysissessionresult.util.shimmeringEffect

@Composable
fun TitleShimmeringLoading(
    modifier: Modifier = Modifier,
    height: Dp = 17.dp,
    widthRatio: Float = 0.2f
) {
    Box(
        modifier = modifier
            .fillMaxWidth(widthRatio)
            .height(height)
            .shimmeringEffect()
    )
}

@Preview
@Composable
private fun TitleShimmeringLoadingPreview() {
    KakaoXpertTheme {
        TitleShimmeringLoading()
    }
}