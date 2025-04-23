package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.cacaoimagedetail.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.BoundingBox

@Composable
fun OverlayCompose(
    modifier: Modifier = Modifier,
    boundingBoxes: List<BoundingBox>
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            OverlayView(context, null)
        },
        update = { view ->
            view.apply {
                setResults(boundingBoxes)
            }
        },
        onRelease = { view ->
            view.clear()
        }
    )
}