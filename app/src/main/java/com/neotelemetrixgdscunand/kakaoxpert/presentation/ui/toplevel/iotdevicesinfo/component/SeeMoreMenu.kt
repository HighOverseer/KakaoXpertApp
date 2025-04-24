package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.toplevel.iotdevicesinfo.component

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.neotelemetrixgdscunand.kakaoxpert.R
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Green55
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.KakaoXpertTheme
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.util.ImagePainterStable

@Composable
fun SeeMoreMenu(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    val imageModifier = remember {
        Modifier
            .clip(CircleShape)
            .border(width = 1.dp, color = Color.White, shape = CircleShape)
            .width(44.dp)
            .widthIn(32.dp, 64.dp)
            .aspectRatio(1f)
            .clickable(onClick = onClick)
    }

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = Green55
        ),
        onClick = onClick,
        shape = RoundedCornerShape(8.dp),
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Box(
                modifier = imageModifier
            ) {
                ImagePainterStable(
                    modifier = Modifier
                        .align(Alignment.Center),
                    drawableResId = R.drawable.ic_right_arrow,
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(Color.White)
                )
            }

            Spacer(Modifier.weight(1f))

            Text(
                text = stringResource(R.string.lebih_lengkap),
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.lihat_secara_waktu),
                style = MaterialTheme.typography.labelMedium,
                color = Color.White,
            )
        }
    }
}

@Preview
@Composable
private fun SeeMoreMenuPreview() {
    KakaoXpertTheme {
        SeeMoreMenu(
            Modifier
                .width(181.dp)
                .aspectRatio(1.1f)
        )
    }
}