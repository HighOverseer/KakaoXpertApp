package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.takephoto.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Black10
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Grey69
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.KakaoXpertTheme

@Composable
fun TertiaryButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    text: String = "",
    cornerRadius: Dp = 4.dp
) {
    Button(
        modifier = modifier,
        colors = ButtonDefaults
            .buttonColors(
                containerColor = Grey69,
                contentColor = Black10
            ),
        onClick = onClick,
        shape = RoundedCornerShape(cornerRadius),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        Text(
            text,
            style = MaterialTheme.typography.labelMedium,
            color = Black10
        )
    }
}

@Preview
@Composable
private fun TertiaryButtonPreview() {
    KakaoXpertTheme {
        TertiaryButton(
            modifier = Modifier

        )
    }

}