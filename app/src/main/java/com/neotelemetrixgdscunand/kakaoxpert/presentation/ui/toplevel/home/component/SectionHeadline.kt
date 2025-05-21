package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.toplevel.home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.neotelemetrixgdscunand.kakaoxpert.R
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Grey65
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Grey69
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Grey90
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.KakaoXpertTheme

@Composable
fun SectionHeadline(
    modifier: Modifier = Modifier,
    leadingIconResId: Int? = null,
    trailingIconResId: Int? = null,
    onTrailingIconClicked: () -> Unit = {},
    title: String = "Test"
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
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
            IconButton(
                onClick = onTrailingIconClicked
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(it),
                    tint = Grey69,
                    contentDescription = null
                )
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
private fun SectionHeadlinePreview() {
    KakaoXpertTheme {
        SectionHeadline(
            Modifier.padding(horizontal = 16.dp),
            leadingIconResId = R.drawable.ic_camera_menu,
            trailingIconResId = R.drawable.ic_right_arrow,
        )
    }

}