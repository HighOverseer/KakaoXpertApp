package com.neotelemetrixgdscunand.kakaoxpert.presentation.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage

@Composable
fun AsyncImagePainterStable(
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Fit,
    contentDescription: String? = null,
    imageUrlOrPath: String? = "",
    colorFilter: androidx.compose.ui.graphics.ColorFilter? = null,
    alignment: Alignment = Alignment.Center,
    placeholderResId: Int,
) {
    AsyncImage(
        modifier = modifier,
        model = imageUrlOrPath,
        alignment = alignment,
        placeholder = painterResource(placeholderResId),
        contentScale = contentScale,
        contentDescription = contentDescription,
        colorFilter = colorFilter
    )
}

@Composable
fun AsyncImagePainterStable(
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Fit,
    contentDescription: String? = null,
    imageDrawableResId: Int? = null,
    colorFilter: androidx.compose.ui.graphics.ColorFilter? = null,
    alignment: Alignment = Alignment.Center,
    placeholderResId: Int? = null,
) {
    AsyncImage(
        modifier = modifier,
        model = imageDrawableResId,
        alignment = alignment,
        placeholder = placeholderResId?.let { painterResource(it) },
        contentScale = contentScale,
        contentDescription = contentDescription,
        colorFilter = colorFilter
    )
}