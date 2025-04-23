package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.auth.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.neotelemetrixgdscunand.kakaoxpert.R
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Green55
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.KakaoXpertTheme
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Orange85
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.montserratFamily


@Composable
fun SmallLogoHeadline(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier
                .width(31.dp)
                .height(31.dp),
            imageVector = ImageVector
                .vectorResource(R.drawable.ic_logo),
            contentDescription = stringResource(R.string.logo)
        )

        Spacer(modifier = Modifier.width(4.dp))

        Text(
            text = stringResource(R.string.kakao_),
            style = MaterialTheme.typography.headlineSmall.copy(
                fontFamily = montserratFamily
            ),
            color = Green55
        )
        Text(
            text = stringResource(R.string.xpert),
            style = MaterialTheme.typography.headlineSmall.copy(
                fontFamily = montserratFamily
            ),
            color = Orange85
        )
    }
}

@Preview
@Composable
private fun SmallLogoHeadlinePreview() {
    KakaoXpertTheme {
        SmallLogoHeadline()
    }

}