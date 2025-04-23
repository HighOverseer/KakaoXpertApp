package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.news

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.neotelemetrixgdscunand.kakaoxpert.R
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Black10
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Black50
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Grey53
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.KakaoXpertTheme
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.util.AsyncImagePainterStable

@Composable
fun NewsDetailScreen(
    modifier: Modifier = Modifier,
    navigateUp: () -> Unit = {},

    ) {

    val scrollState = rememberScrollState()
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
    ) {

        Spacer(Modifier.height(32.dp))

        IconButton(
            onClick = navigateUp
        ) {
            Icon(
                modifier = Modifier
                    .width(21.dp)
                    .height(20.dp),
                imageVector = ImageVector
                    .vectorResource(R.drawable.ic_arrow_left),
                contentDescription = null,
                tint = Black10
            )
        }

        Spacer(Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, bottom = 40.25.dp)

        ) {

            val imageAspectRatio = 1.652f
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(15.dp))
            ) {
                AsyncImagePainterStable(
                    modifier = Modifier
                        .aspectRatio(imageAspectRatio)
                        .align(Alignment.Center),
                    imageUrlOrPath = "https://akcdn.detik.net.id/visual/2023/10/13/tanaman-kakao_169.jpeg?w=715&q=90",
                    placeholderResId = R.drawable.ic_camera,
                    contentScale = ContentScale.FillBounds,
                    contentDescription = stringResource(R.string.gambar_berita)
                )
            }

            Spacer(Modifier.height(24.dp))

            Row(
                verticalAlignment = Alignment.Top
            ) {

                Icon(
                    modifier = Modifier
                        .align(Alignment.CenterVertically),
                    imageVector = ImageVector
                        .vectorResource(R.drawable.ic_clock_2),
                    tint = Color(0xff908E8E),
                    contentDescription = null
                )

                Spacer(Modifier.width(8.dp))

                Text(
                    modifier = Modifier
                        .offset(y = (-1).dp),
                    text = stringResource(R.string._21_september_2024),
                    style = MaterialTheme.typography.labelMedium,
                    color = Grey53
                )
            }

            Spacer(Modifier.height(16.dp))

            Text(
                stringResource(R.string.dummy_news_title),
                style = MaterialTheme.typography.titleLarge,
                color = Black10,

                )

            Spacer(Modifier.height(24.dp))

            Text(
                textAlign = TextAlign.Justify,
                text = stringResource(R.string.dummy_news_detail),
                style = MaterialTheme.typography.bodyLarge,
                color = Black50
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
private fun NewsDetailScreenPreview() {
    KakaoXpertTheme {
        NewsDetailScreen()
    }
}