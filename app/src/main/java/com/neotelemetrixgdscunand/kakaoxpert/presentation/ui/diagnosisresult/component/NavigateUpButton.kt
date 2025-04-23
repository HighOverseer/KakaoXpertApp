package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.diagnosisresult.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.neotelemetrixgdscunand.kakaoxpert.R
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.KakaoXpertTheme
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.WhiteTransparent

@Composable
fun NavigateUpButton(
    modifier: Modifier = Modifier,
    topToArrowMargin: Dp = 0.dp,
    navigateUp: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .padding(start = 16.dp, top = topToArrowMargin)
            .clip(CircleShape)
            .background(color = WhiteTransparent)
            .size(37.dp)
            .clickable(
                onClick = navigateUp
            )
    ) {
        Image(
            modifier = Modifier
                .align(Alignment.Center)
                .size(15.dp),
            imageVector = ImageVector
                .vectorResource(R.drawable.ic_arrow_left),
            contentDescription = stringResource(R.string.kembali)
        )
    }
}

@Preview
@Composable
private fun NavigateUpButtonPreview() {
    KakaoXpertTheme {
        NavigateUpButton()
    }

}