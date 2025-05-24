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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.neotelemetrixgdscunand.kakaoxpert.R
import com.neotelemetrixgdscunand.kakaoxpert.presentation.dui.NewsDetailsDui
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Black10
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Black50
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Grey53
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.KakaoXpertTheme
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.analysissessionresult.util.shimmeringEffect
import com.neotelemetrixgdscunand.kakaoxpert.presentation.utils.AsyncImagePainterStable
import com.neotelemetrixgdscunand.kakaoxpert.presentation.utils.collectChannelWhenStarted

@Composable
fun NewsDetailsScreen(
    modifier: Modifier = Modifier,
    navigateUp: () -> Unit = {},
    showSnackbar: (String) -> Unit = {},
    viewModel: NewsDetailViewModel = hiltViewModel()
) {

    val lifecycle = LocalLifecycleOwner.current
    val onFailedGettingDetails = stringResource(R.string.maaf_detail_berita_tidak_ditemukan)
    LaunchedEffect(true) {
        lifecycle.collectChannelWhenStarted(
            viewModel.onFailedGettingSelectedNewsDetailsEvent
        ) {
            showSnackbar(onFailedGettingDetails)
            navigateUp()
        }
    }

    val newsDetails by viewModel.newsDetails.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()

    NewsDetailsContent(
        modifier = modifier,
        navigateUp = navigateUp,
        newsDetails = newsDetails,
        isLoading = isLoading
    )
}

@Composable
fun NewsDetailsContent(
    modifier: Modifier = Modifier,
    navigateUp: () -> Unit = {},
    newsDetails: NewsDetailsDui? = null,
    isLoading: Boolean = false
) {

    if (isLoading) {
        NewsDetailsContentLoading(
            navigateUp = navigateUp
        )
    } else {
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
                        imageUrlOrPath = newsDetails?.imageUrl,
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
                        text = newsDetails?.date ?: "-",
                        style = MaterialTheme.typography.labelMedium,
                        color = Grey53
                    )
                }

                Spacer(Modifier.height(16.dp))

                Text(
                    newsDetails?.headline ?: "",
                    style = MaterialTheme.typography.titleLarge,
                    color = Black10,
                )

                Spacer(Modifier.height(24.dp))

                Text(
                    textAlign = TextAlign.Justify,
                    text = newsDetails?.description ?: "",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Black50
                )
            }

        }
    }
}

@Composable
private fun NewsDetailsContentLoading(
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
                Box(
                    modifier = Modifier
                        .aspectRatio(imageAspectRatio)
                        .align(Alignment.Center)
                        .shimmeringEffect(),
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

                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.25f)
                        .height(17.dp)
                        .shimmeringEffect()
                )
            }

            Spacer(Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(17.dp)
                    .shimmeringEffect()
            )

            Spacer(Modifier.height(24.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(24.dp)
                    .shimmeringEffect()
            )

            Spacer(Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(24.dp)
                    .shimmeringEffect()
            )

            Spacer(Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(24.dp)
                    .shimmeringEffect()
            )


            Spacer(Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(24.dp)
                    .shimmeringEffect()
            )

            Spacer(Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .height(24.dp)
                    .shimmeringEffect()
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
private fun NewsDetailScreenPreview() {
    KakaoXpertTheme {
        NewsDetailsContent()
    }
}

@Preview(showBackground = true)
@Composable
private fun NewsDetailScreenLoadingPreview() {
    KakaoXpertTheme {
        NewsDetailsContentLoading()
    }
}