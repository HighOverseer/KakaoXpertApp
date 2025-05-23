package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.diagnosisresult.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Black10
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Grey60
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.KakaoXpertTheme

@Composable
fun PrimaryDescription(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    titleTextColor: Color = Black10,
    descriptionTextColor: Color = Grey60,
    descriptionTextAlign: TextAlign? = null
) {
    Text(
        modifier = modifier,
        text = title,
        style = MaterialTheme.typography.titleMedium,
        color = titleTextColor
    )

    Spacer(Modifier.height(8.dp))

    Text(
        description,
        style = MaterialTheme.typography.labelMedium,
        color = descriptionTextColor,
        textAlign = descriptionTextAlign
    )
}

@Preview
@Composable
private fun PrimaryDescriptionPreview() {
    KakaoXpertTheme {
        PrimaryDescription(title = "", description = "")
    }
}