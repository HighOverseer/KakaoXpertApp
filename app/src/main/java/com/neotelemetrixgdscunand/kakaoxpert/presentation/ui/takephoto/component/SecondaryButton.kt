package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.takephoto.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Grey60
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.KakaoXpertTheme

@Composable
fun SecondaryButton(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(16.dp),
    onClick: () -> Unit = {},
    text: String = "",
    textColor: Color = Grey60,
    containerColor: Color = Color.Transparent,
    borderColor: Color = Grey60,
    textStyle: TextStyle = MaterialTheme.typography.titleMedium,
    enabled: Boolean = true
) {
    Button(
        modifier = modifier,
        contentPadding = contentPadding,
        shape = RoundedCornerShape(8.dp),
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            contentColor = Grey60,
            containerColor = containerColor,
        ),
        border = BorderStroke(width = 1.dp, color = borderColor)
    ) {
        Text(
            text,
            style = textStyle,
            color = textColor,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SecondaryButtonPreview() {
    KakaoXpertTheme {
        SecondaryButton(
            text = "Tes",
            contentPadding = PaddingValues(horizontal = 32.dp, vertical = 4.dp)
        )
    }
}