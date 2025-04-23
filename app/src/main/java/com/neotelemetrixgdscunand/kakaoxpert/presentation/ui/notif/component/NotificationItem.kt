package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.notif.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.neotelemetrixgdscunand.kakaoxpert.R
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Black30
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Green55
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Grey47
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Grey60
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Grey75
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.KakaoXpertTheme
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Orange90
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.takephoto.component.SecondaryButton
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.util.AsyncImagePainterStable
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.util.ImagePainterStable

@Composable
fun NotificationItem(
    modifier: Modifier = Modifier,
    currentAmountFulfilled: Float,
    exporterName: String = "-",
    sendDate: String = "-",
    exporterImageUrl: String = "",
    totalAmountDemands: Float,
    isAgreed: Boolean = false,
    message: String = "-",
    navigateToDetail: () -> Unit = { }
) {

    val isFulfilled = remember(currentAmountFulfilled, totalAmountDemands) {
        currentAmountFulfilled == totalAmountDemands
    }

    val contentMarginModifier = remember {
        if (isAgreed) {
            Modifier.height(12.dp)
        } else Modifier.height(8.dp)
    }

    Column(
        modifier
            .background(color = Color.White, shape = RoundedCornerShape(12.dp))
            .padding(vertical = 24.dp, horizontal = 16.dp)
    ) {
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            AsyncImagePainterStable(
                imageUrlOrPath = exporterImageUrl,
                alignment = Alignment.Center,
                contentScale = ContentScale.Crop,
                contentDescription = null,
                placeholderResId = R.drawable.ic_camera,
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
            )

            Spacer(Modifier.width(16.dp))

            Column(Modifier.fillMaxWidth()) {
                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        exporterName,
                        style = MaterialTheme.typography.titleMedium.copy(
                            lineHeight = 15.sp,
                            letterSpacing = (-0.02).sp
                        ),
                        color = Orange90,
                        modifier = Modifier.weight(1f)
                    )

                    Text(
                        sendDate,
                        style = MaterialTheme.typography.labelMedium.copy(
                            lineHeight = 20.sp,
                            letterSpacing = (-0.02).sp
                        ),
                        color = Grey60
                    )
                }

                Spacer(contentMarginModifier)


                Row(
                    Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(
                            R.string.ton_terpenuhi,
                            currentAmountFulfilled,
                            totalAmountDemands
                        ),
                        style = MaterialTheme.typography.bodySmall.copy(
                            lineHeight = 20.sp,
                            letterSpacing = (-0.02).sp,
                            fontStyle = FontStyle.Italic
                        ),
                        color = Green55,
                        modifier = Modifier.weight(1f)
                    )

                    if (isAgreed) {
                        ImagePainterStable(
                            drawableResId = R.drawable.ic_verified,
                            contentScale = ContentScale.Fit,
                            contentDescription = null
                        )

                        Spacer(Modifier.width(8.dp))

                        Text(
                            stringResource(R.string.telah_disanggupi),
                            style = MaterialTheme.typography.bodySmall.copy(
                                lineHeight = 16.sp,
                                letterSpacing = 0.02.sp
                            )
                        )
                    }
                }

                Spacer(contentMarginModifier)

                Text(
                    message,
                    style = MaterialTheme.typography.labelMedium.copy(
                        lineHeight = 20.sp,
                        letterSpacing = (-0.02).sp
                    ),
                    color = Black30
                )

                Spacer(Modifier.height(24.dp))

                if (isFulfilled) {
                    SecondaryButton(
                        text = stringResource(R.string.telah_terpenuhi),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(45.dp),
                        borderColor = Grey75,
                        textColor = Grey47,
                        contentPadding = PaddingValues(0.dp),
                        textStyle = MaterialTheme.typography.titleMedium,
                        containerColor = Grey75,
                        onClick = { }
                    )
                } else {
                    SecondaryButton(
                        text = stringResource(R.string.cek_detail),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(45.dp),
                        contentPadding = PaddingValues(0.dp),
                        borderColor = Green55,
                        textColor = Green55,
                        textStyle = MaterialTheme.typography.titleMedium,
                        containerColor = Color.Transparent,
                        onClick = navigateToDetail
                    )
                }

            }
        }
    }
}

@Preview(showBackground = false)
@Composable
private fun NotificationItemPreview() {
    KakaoXpertTheme {
        NotificationItem(
            currentAmountFulfilled = 10f,
            totalAmountDemands = 10f,
            isAgreed = true,
            exporterImageUrl = "https://s3-alpha-sig.figma.com/img/7bb8/e06d/7ba6df889cfe83fee1003b145be98bb3?Expires=1737936000&Key-Pair-Id=APKAQ4GOSFWCVNEHN3O4&Signature=oO74RgJwXOdzxaU9NVzA4Mt0Rt3VsxbvX2PAkkl7qPo2Sx8-8trQ4ARbZ4LcfBd9SLHVG9jYnyj6~RVY~QAGjUmE4yjExH5GSdZkpe6J8tPPRaRh7o1lGEApTmw9WX7Vh82yz0qioLfk4WKWpKvVEqnHrRM-U-sOCoGe-Vw41Y4~XrOuugCNmJ4DXAB1N7bn8ds1FWg1m1u5PAWZ2-UGc~mJLtBcCzEKZ~oaM35D9xSLRNhRgYx6oiLt~sjdmM0sbi6RqKqRNP-HyumNuRyeYJNp5PH0y6K7WCgdGfH6NghnDapXYtU22eoY9WNbPD6JtbmhZUYl8DZkhEieYb0CvA__",
            sendDate = "Jan 24",
            exporterName = "Anthony Gasing",
            message = "Membutuhkan 10 Ton Kakao dengan varietas Criollo tawaran harga sebesar \$25.000",
        )
    }
}