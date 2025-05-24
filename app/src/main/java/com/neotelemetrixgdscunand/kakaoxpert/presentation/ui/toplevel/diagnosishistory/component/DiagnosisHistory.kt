package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.toplevel.diagnosishistory.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.neotelemetrixgdscunand.kakaoxpert.R
import com.neotelemetrixgdscunand.kakaoxpert.presentation.dui.AnalysisSessionPreviewDui
import com.neotelemetrixgdscunand.kakaoxpert.presentation.mapper.DuiMapper.formatToIdrCurrency
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Black10
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Green55
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Grey65
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.KakaoXpertTheme
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Orange85
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.diagnosisresult.util.formatSellPriceEstimationForHistory
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.diagnosisresult.util.roundOffDecimal
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.util.AsyncImagePainterStable

@Composable
fun DiagnosisHistory(
    modifier: Modifier = Modifier,
    item: AnalysisSessionPreviewDui,
    onClick: () -> Unit = { }
) {
    val cardModifier = remember {
        modifier
            .background(color = Color.White, shape = RoundedCornerShape(8.dp))
            .padding(16.dp)
    }

    val imageModifier = remember {
        Modifier
            .width(103.dp)
            .height(92.dp)
            .clip(RoundedCornerShape(8.dp))
    }

    val cardColors = CardDefaults.cardColors(
        containerColor = Color.White,
        contentColor = Color.White
    )

    Card(
        modifier = cardModifier,
        colors = cardColors,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = imageModifier
                    .align(Alignment.CenterVertically)
            ) {
                AsyncImagePainterStable(
                    modifier = Modifier
                        .align(Alignment.Center),
                    imageUrlOrPath = item.imageUrlOrPath,
                    contentScale = ContentScale.Crop,
                    contentDescription = item.title,
                    placeholderResId = R.drawable.ic_camera
                )
            }

            Spacer(Modifier.width(16.dp))

            Column {
                Text(
                    item.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = Black10,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text = "~${item.predictedPrice.formatToIdrCurrency()}",
                    style = MaterialTheme.typography.labelMedium,
                    color = Black10
                )

                Spacer(
                    Modifier.height(8.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Image(
                        imageVector = ImageVector
                            .vectorResource(R.drawable.ic_calendar),
                        contentDescription = null
                    )

                    Spacer(Modifier.width(8.dp))

                    Text(
                        item.date,
                        style = MaterialTheme.typography.labelMedium,
                        color = Grey65
                    )
                }

                Spacer(Modifier.height(12.dp))

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
                            modifier = Modifier.size(8.dp),
                            imageVector = ImageVector.vectorResource(R.drawable.ic_sync),
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
                            imageVector = if (item.availableOffline) Icons.Default.Check else Icons.Default.Close,
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

                Spacer(
                    Modifier.height(10.dp)
                )
            }

        }
    }
}

@Preview
@Composable
private fun DiagnosisHistoryPreview() {
    KakaoXpertTheme {
        DiagnosisHistory(
            item = AnalysisSessionPreviewDui(
                id = 0,
                title = "Kakao Pak Tono",
                imageUrlOrPath = "https://drive.google.com/file/d/1SXCPCoMzRjZEpemeT-mLOUTD2mzbGee_/view?usp=drive_link",
                date = "12-11-2024",
                predictedPrice = 700f,
                hasSynced = true,
                availableOffline = false
            )
        )
    }

}

