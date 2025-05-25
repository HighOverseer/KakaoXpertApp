package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.analysissessionresult.diseasediagnosis.component

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.neotelemetrixgdscunand.kakaoxpert.R
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Black10
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Green55
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Grey60
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Grey69
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.KakaoXpertTheme
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Orange90
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.analysissessionresult.component.SecondaryDescription
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.analysissessionresult.component.TitleShimmeringLoading
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun DiagnosisBottomContent(
    modifier: Modifier = Modifier,
    solution: String = "-",
    preventions: ImmutableList<String> = persistentListOf(),
    isLoadingProvider: () -> Boolean = { false },
    isSolutionsFromLLMProvider: () -> Boolean = { false }
) {
    if (isLoadingProvider()) {
        DiagnosisBottomContentLoading(modifier = modifier)

    } else Column(modifier = modifier) {
        val isFromLLM = isSolutionsFromLLMProvider()
        if (isFromLLM) {
            Row(
                Modifier
                    .background(color = Color.White, shape = RoundedCornerShape(4.dp)),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Image(
                    imageVector = ImageVector
                        .vectorResource(R.drawable.ic_checklist),
                    contentDescription = null
                )


                Spacer(Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.berdasarkan_data_sensor_perangkat_iot),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 14.sp
                    ),
                    color = Green55,
                )
            }

            Spacer(Modifier.height(16.dp))

            Spacer(
                Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(color = Grey69)
            )

            Spacer(Modifier.height(16.dp))
        }

        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                imageVector = ImageVector
                    .vectorResource(R.drawable.ic_sprinkle_gradient),
                contentDescription = null,
            )

            Spacer(Modifier.width(8.dp))

            Text(
                stringResource(R.string.yang_harus_kamu_lakukan),
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    brush = Brush.linearGradient(
                        colorStops = arrayOf(
                            Pair(0f, Green55),
                            Pair(1f, Orange90)
                        ),
                    )
                ),
            )
        }



        Spacer(Modifier.height(16.dp))

        SecondaryDescription(
            title = stringResource(R.string.solusi),
            description = solution,
            descriptionTextAlign = TextAlign.Justify
        )

        Spacer(Modifier.height(24.dp))

        Text(
            stringResource(R.string.pencegahan),
            style = MaterialTheme.typography.titleMedium,
            color = Black10
        )

        Spacer(Modifier.height(8.dp))

        preventions.forEachIndexed { index, it ->
            if (index != 0) {
                Spacer(Modifier.height(16.dp))
            }

            Row(
                Modifier.fillMaxWidth()
            ) {

                if (index != 0 && (index != preventions.lastIndex || it.contains(':'))) {
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .clip(CircleShape)
                                .size(4.dp)
                                .background(color = Grey60)
                        )
                    }
                }

                Text(
                    it,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 14.sp
                    ),
                    color = Grey60,
                    textAlign = TextAlign.Justify
                )
            }

        }
    }
}

@Composable
fun DiagnosisBottomContentLoading(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {


            Icon(
                contentDescription = null,
                imageVector = ImageVector
                    .vectorResource(R.drawable.ic_sprinkle_gradient),
                tint = Orange90
            )


            var textSize by remember {
                mutableStateOf(IntSize.Zero)
            }
            val transition = rememberInfiniteTransition(label = "shimmering_transition")
            val startOffsetX by transition.animateFloat(
                initialValue = -1 * textSize.width.toFloat(),
                targetValue = 1 * textSize.width.toFloat(),
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        1000,
                        easing = LinearEasing
                    ),
                ),
                label = "shimmering_transition"
            )



            Spacer(Modifier.width(8.dp))

            Text(
                modifier = Modifier
                    .onGloballyPositioned {
                        textSize = it.size
                    },
                text = stringResource(R.string.yang_harus_kamu_lakukan),
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Green55,
                            Orange90,
                            Green55,
                        ),
                        start = Offset(startOffsetX, 0f),
                        end = Offset(
                            startOffsetX + textSize.width.toFloat(),
                            textSize.height.toFloat()
                        )
                    )
                ),
            )
        }

        Spacer(Modifier.height(16.dp))

        TitleShimmeringLoading(
            widthRatio = 0.2f,
            height = 17.dp
        )

        Spacer(Modifier.height(8.dp))

        DescriptionShimmeringLoading(
            lineHeight = 16.dp,
            lastLineWidthRatio = 0.7f,
            lineCount = 5
        )

        Spacer(Modifier.height(24.dp))

        TitleShimmeringLoading(
            widthRatio = 0.4f,
            height = 17.dp
        )

        Spacer(Modifier.height(8.dp))

        DescriptionShimmeringLoading(
            lineHeight = 16.dp,
            lastLineWidthRatio = 0.7f,
            lineCount = 9
        )

    }
}

@Preview(showBackground = true)
@Composable
private fun DiagnosisBottomContentPreview() {
    KakaoXpertTheme { DiagnosisBottomContent() }
}