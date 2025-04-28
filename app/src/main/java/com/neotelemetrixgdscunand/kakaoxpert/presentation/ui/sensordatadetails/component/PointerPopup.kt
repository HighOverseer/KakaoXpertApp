package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.sensordatadetails.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Popup
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Grey69

@Composable
fun PointerPopup(
    onDismissRequest: () -> Unit = {},
    isVisibleProvider: () -> Boolean = { false },
    textProvider: () -> String? = { "" }
) {
    val isVisible = isVisibleProvider()
    val text = if(isVisible) textProvider() else null

    if (text != null) {
        Popup(
            alignment = Alignment.BottomEnd,
            onDismissRequest = onDismissRequest
        ) {
            Column(
                Modifier.background(Grey69)
            ) {
                Text(text)
            }
        }
    }
}
