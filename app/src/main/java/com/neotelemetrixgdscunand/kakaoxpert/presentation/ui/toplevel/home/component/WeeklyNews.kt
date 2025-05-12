package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.toplevel.home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.neotelemetrixgdscunand.kakaoxpert.R
import com.neotelemetrixgdscunand.kakaoxpert.presentation.dui.NewsItemDui
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Black10
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Grey65
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.KakaoXpertTheme
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.diagnosisresult.util.shimmeringEffect
import com.neotelemetrixgdscunand.kakaoxpert.presentation.utils.AsyncImagePainterStable

@Composable
fun WeeklyNews(
    modifier: Modifier = Modifier,
    item: NewsItemDui,
    onClick: () -> Unit = {}
) {
    val cardModifier = remember {
        modifier
            .height(110.dp)
            .background(color = Color.White, shape = RoundedCornerShape(8.dp))
            .padding(12.dp)
    }

    val imageModifier = remember {
        Modifier
            .width(104.dp)
            .clip(RoundedCornerShape(8.dp))
    }

    Card(
        modifier = cardModifier,
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
            contentColor = Color.White
        ),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = imageModifier
                    .align(Alignment.CenterVertically)
                    .fillMaxSize()
            ) {
                AsyncImagePainterStable(
                    modifier = Modifier
                        .align(Alignment.Center),
                    imageUrlOrPath = item.imageUrl,
                    contentScale = ContentScale.Crop,
                    contentDescription = item.headline,
                    placeholderResId = R.drawable.ic_camera
                )
            }

            Spacer(Modifier.width(16.dp))

            Column {
                Text(
                    item.headline,
                    style = MaterialTheme.typography.titleMedium.copy(
                        lineHeight = 16.sp,
                        letterSpacing = 0.01.sp
                    ),
                    color = Black10,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(
                    Modifier.height(16.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Image(
                        imageVector = ImageVector
                            .vectorResource(R.drawable.ic_clock_2),
                        contentDescription = null
                    )

                    Spacer(Modifier.width(8.dp))

                    Text(
                        item.date,
                        style = MaterialTheme.typography.labelMedium,
                        color = Grey65
                    )

                }
            }

        }
    }
}

@Composable
fun WeeklyNewsLoading(modifier: Modifier = Modifier) {
    val cardModifier = remember {
        modifier
            .height(110.dp)
            .background(color = Color.White, shape = RoundedCornerShape(8.dp))
            .padding(12.dp)
    }

    val imageModifier = remember {
        Modifier
            .width(104.dp)
            .clip(RoundedCornerShape(8.dp))
    }

    val shimmeringBoxModifier = remember {
        modifier
            .height(17.dp)
            .shimmeringEffect()
    }

    Card(
        modifier = cardModifier,
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
            contentColor = Color.White
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = imageModifier
                    .align(Alignment.CenterVertically)
                    .fillMaxHeight()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .shimmeringEffect()
                )
            }

            Spacer(Modifier.width(16.dp))

            Column {
                Box(
                    modifier = shimmeringBoxModifier
                        .fillMaxWidth()
                )

                Spacer(
                    Modifier.height(16.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Image(
                        imageVector = ImageVector
                            .vectorResource(R.drawable.ic_clock_2),
                        contentDescription = null
                    )

                    Spacer(Modifier.width(8.dp))

                    Box(modifier = shimmeringBoxModifier.fillMaxWidth())

                }
            }

        }
    }
}

@Preview
@Composable
private fun WeeklyNewsPreview() {
    KakaoXpertTheme {
        WeeklyNews(
            item = NewsItemDui(
                id = 0,
                headline = "Chocolate Faces Shortages, Cocoa Prices Soar by 98% in 3 Months",
                date = "22 Mar 2025",
                imageUrl = "",
            )
        )
    }

}

@Preview
@Composable
private fun WeeklyNewsLoadingPreview() {
    KakaoXpertTheme {
        WeeklyNewsLoading()
    }
}

