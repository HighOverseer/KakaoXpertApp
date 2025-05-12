package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.diagnosisresult.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Black10
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Grey60
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.KakaoXpertTheme

@Composable
fun SecondaryDescription(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    descriptionTextAlign:TextAlign? = null
) {
    Text(
        modifier = modifier,
        text = title,
        style = MaterialTheme.typography.titleMedium,
        color = Black10
    )

    Spacer(Modifier.height(8.dp))

    Text(
        description,
        style = MaterialTheme.typography.bodyMedium.copy(
            fontSize = 14.sp
        ),
        color = Grey60,
        textAlign = descriptionTextAlign
    )


}

@Preview
@Composable
private fun SecondaryDescriptionPreview() {
    KakaoXpertTheme {
        SecondaryDescription(title = "", description = "")
    }
}