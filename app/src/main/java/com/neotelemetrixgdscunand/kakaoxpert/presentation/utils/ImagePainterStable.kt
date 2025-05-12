package com.neotelemetrixgdscunand.kakaoxpert.presentation.utils

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource

@Composable
fun ImagePainterStable(
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Fit,
    contentDescription: String? = null,
    drawableResId: Int,
    colorFilter: androidx.compose.ui.graphics.ColorFilter? = null
) {
    Image(
        modifier = modifier,
        painter = painterResource(drawableResId),
        contentScale = contentScale,
        contentDescription = contentDescription,
        colorFilter = colorFilter
    )
}