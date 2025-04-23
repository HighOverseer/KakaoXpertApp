package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.auth.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Grey70
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.KakaoXpertTheme
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Green55

@Composable
fun PrimaryButton(
    modifier: Modifier = Modifier, text: String = "",
    onClick: () -> Unit = {},
    contentPadding: PaddingValues = PaddingValues(vertical = 14.dp),
    trailingIcon: (@Composable () -> Unit)? = null,
    enabled: Boolean = true
) {

    ElevatedButton(
        modifier = modifier,
        contentPadding = contentPadding,
        shape = RoundedCornerShape(8.dp),
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.elevatedButtonColors(
            containerColor = Green55,
            disabledContentColor = Grey70,
        ),
    ) {

        if (enabled) {
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )
        } else {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(16.dp),
                strokeWidth = 2.dp
            )

        }


        if (trailingIcon != null) {
            Spacer(Modifier.width(8.dp))
            trailingIcon()
        }
    }
}

@Preview
@Composable
private fun PrimaryButtonPreview() {
    KakaoXpertTheme {
        PrimaryButton()
    }
}