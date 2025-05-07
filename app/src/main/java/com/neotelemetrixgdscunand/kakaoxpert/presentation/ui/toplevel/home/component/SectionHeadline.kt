package com.neotelemetrixgdscunand.kamekapp.presentation.ui.toplevel.home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.KakaoXpertTheme

@Composable
fun SectionHeadline(
    modifier: Modifier = Modifier,
    leadingIconResId: Int? = null,
    trailingIconResId: Int? = null,
    title: String = "Test"
) {
    Row(
        modifier = modifier
    ) {
        leadingIconResId?.let {
            Image(
                imageVector = ImageVector
                    .vectorResource(it),
                contentDescription = null
            )
            Spacer(Modifier.width(8.dp))
        }

        Text(
            title,
            style = MaterialTheme.typography.headlineSmall
        )

        trailingIconResId?.let {
            Spacer(Modifier.weight(1f))
            Image(
                imageVector = ImageVector.vectorResource(
                    it
                ),
                contentDescription = null
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
private fun SectionHeadlinePreview() {
    KakaoXpertTheme {
        SectionHeadline(
            Modifier.padding(horizontal = 16.dp)
        )
    }

}