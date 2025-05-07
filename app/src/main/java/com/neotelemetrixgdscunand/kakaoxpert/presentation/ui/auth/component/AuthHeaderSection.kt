package com.neotelemetrixgdscunand.kamekapp.presentation.ui.auth.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Green55
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Grey50
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.KakaoXpertTheme

@Composable
fun AuthHeaderSection(modifier: Modifier = Modifier, title: String, subTitle: String) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.displaySmall,
            color = Green55
        )
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = subTitle,
            style = MaterialTheme.typography.labelMedium,
            color = Grey50
        )
    }
}

@Preview
@Composable
private fun AuthHeaderSectionPreview() {
    KakaoXpertTheme {
        AuthHeaderSection(
            title = "Tes",
            subTitle = "Test"
        )
    }

}