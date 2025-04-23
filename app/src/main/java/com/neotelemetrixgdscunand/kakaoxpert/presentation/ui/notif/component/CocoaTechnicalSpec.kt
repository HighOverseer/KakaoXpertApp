package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.notif.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Grey60
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.KakaoXpertTheme

@Composable
fun CacaoTechnicalSpec(
    modifier: Modifier = Modifier,
    titleResId: Int = -1,
    text: String = ""
) {
    Column(modifier = modifier) {
        Text(
            stringResource(titleResId),
            style = MaterialTheme.typography.labelMedium
                .copy(
                    lineHeight = 15.sp,
                    letterSpacing = (-0.02).sp
                )
        )

        Spacer(Modifier.height(16.dp))

        Text(
            text,
            style = MaterialTheme.typography.labelMedium
                .copy(
                    lineHeight = 15.sp,
                    letterSpacing = (-0.02).sp
                ),
            color = Grey60
        )
    }
}

@Preview
@Composable
private fun CacaoTechnicalSpecPreview() {
    KakaoXpertTheme {
        CacaoTechnicalSpec()
    }
}