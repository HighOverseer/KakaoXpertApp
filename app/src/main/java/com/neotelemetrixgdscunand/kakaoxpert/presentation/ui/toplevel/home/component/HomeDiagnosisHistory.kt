package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.toplevel.home.component

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.neotelemetrixgdscunand.kakaoxpert.R
import com.neotelemetrixgdscunand.kakaoxpert.presentation.dui.AnalysisSessionPreviewDui
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Black10
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Green55
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Grey65
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.KakaoXpertTheme
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Orange85
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Red
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Yellow90
import com.neotelemetrixgdscunand.kakaoxpert.presentation.utils.AsyncImagePainterStable
import com.neotelemetrixgdscunand.kakaoxpert.presentation.utils.ImagePainterStable


@Composable
fun HomeDiagnosisHistory(
    modifier: Modifier = Modifier,
    item: AnalysisSessionPreviewDui
) {

    val configuration = LocalConfiguration.current
    val cardMaxWidth = remember {
        val screenWidthDp = configuration.screenWidthDp.dp
        val maxWidth = when (configuration.orientation) {
            Configuration.ORIENTATION_PORTRAIT -> {
                screenWidthDp / 2.25f
            }

            else -> screenWidthDp / 3
        }
        maxWidth
    }

    val cardModifier = remember {
        modifier
            .width(cardMaxWidth)
            .wrapContentHeight()
            .background(color = Color.White, RoundedCornerShape(8.dp))
            .padding(top = 8.dp, start = 8.dp, end = 8.dp, bottom = 16.dp)
    }

    Card(
        shape = RoundedCornerShape(0.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
            contentColor = Color.White
        ),
        modifier = cardModifier

    ) {
        val imageModifier = remember {
            Modifier
                .height(100.dp)
                .clip(RoundedCornerShape(8.dp))
        }
        Box(
            modifier = imageModifier
                .align(Alignment.CenterHorizontally)
        ) {
            AsyncImagePainterStable(
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth(),
                imageUrlOrPath = item.imageUrlOrPath,
                contentScale = ContentScale.Crop,
                contentDescription = item.title,
                placeholderResId = R.drawable.ic_camera
            )
        }

        Spacer(Modifier.height(16.dp))

        Text(
            item.title,
            style = MaterialTheme.typography.titleMedium,
            color = Black10,
            textAlign = TextAlign.Justify,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )


        Spacer(
            Modifier.height(6.dp)
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                imageVector = ImageVector
                    .vectorResource(
                        R.drawable.ic_calendar
                    ),
                contentDescription = stringResource(R.string.kalender)
            )

            Text(
                item.date,
                style = MaterialTheme.typography.labelMedium,
                color = Grey65,
                textAlign = TextAlign.Start
            )
        }

        Spacer(
            Modifier.height(12.dp)
        )

        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .background(
                        if (item.hasSynced) Green55 else Orange85,
                        shape = RoundedCornerShape(4.dp)
                    )
                    .padding(horizontal = 4.dp, vertical = 2.dp)
            ) {
                Image(
                    modifier = Modifier.size(12.dp),
                    imageVector = Icons.Default.Refresh,
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(Color.White)
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = stringResource(R.string.tersinkron),
                    color = Color.White,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Spacer(Modifier.width(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .background(
                        if (item.availableOffline) Green55 else Orange85,
                        shape = RoundedCornerShape(4.dp)
                    )
                    .padding(horizontal = 4.dp, vertical = 2.dp)
            ) {
                Image(
                    modifier = Modifier.size(12.dp),
                    imageVector = if(item.availableOffline) Icons.Default.Check else Icons.Default.Close,
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(Color.White)
                )
                Spacer(Modifier.width(2.dp))
                Text(
                    text = stringResource(R.string.offline),
                    color = Color.White,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

    }
}

@Preview
@Composable
private fun DiagnosisHistoryPreview() {
    KakaoXpertTheme {
        HomeDiagnosisHistory(
            item =
            AnalysisSessionPreviewDui(
                id = 0,
                title = "Kakao Pak Tono",
                imageUrlOrPath = "https://drive.google.com/file/d/1SXCPCoMzRjZEpemeT-mLOUTD2mzbGee_/view?usp=drive_link",
                date = "12/11/2024",
                predictedPrice = 700f,
                hasSynced = true,
                availableOffline = false
            )
        )
    }

}