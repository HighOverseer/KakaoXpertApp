package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.analysissessionresult.priceanalysis.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.neotelemetrixgdscunand.kakaoxpert.R
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.DetectedCocoa
import com.neotelemetrixgdscunand.kakaoxpert.presentation.mapper.DuiMapper.formatToIdrCurrency
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Black10
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.KakaoXpertTheme
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Orange80
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Orange90
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Yellow90
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.analysissessionresult.component.SecondaryDescription
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.analysissessionresult.component.TitleShimmeringLoading
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.analysissessionresult.diseasediagnosis.component.DescriptionShimmeringLoading
import com.neotelemetrixgdscunand.kakaoxpert.presentation.utils.dashedBorder
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableMap

@Composable
fun PriceAnalysisContent(
    modifier: Modifier = Modifier,
    isInitiallyExpanded: Boolean = false,
    groupedDamagedLevelToDetectedCocoa: ImmutableMap<Int, ImmutableList<DetectedCocoa>> =
        emptyMap<Int, ImmutableList<DetectedCocoa>>().toImmutableMap(),
   // damageLevelCategory: DamageLevelCategory = DamageLevelCategory.High,
    onDetectedCacaoImageClicked: (Int) -> Unit = { },
    diseaseName:String = "",
    cocoaAverageWeightInputProvider:()-> String = { "0.2f" },
) {

    var isExpand by remember {
        mutableStateOf(isInitiallyExpanded)
    }

    val detectedCocoaCount = remember(groupedDamagedLevelToDetectedCocoa) {
        groupedDamagedLevelToDetectedCocoa.values.sumOf { it.size }
    }

    val subTotalPrice = remember(groupedDamagedLevelToDetectedCocoa) {
        groupedDamagedLevelToDetectedCocoa.values.sumOf {
            it.sumOf { it.predictedPriceInIdr.toDouble() }
        }.formatToIdrCurrency()
    }


    Column(
        modifier = modifier
            .background(color = Color.White, shape = RoundedCornerShape(8.dp))
            .padding(16.dp),
    ) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(
                diseaseName,
                style = MaterialTheme.typography.titleMedium,
                color = Orange80,
                modifier = Modifier.weight(1f)
            )

            IconButton(
                onClick = {
                    isExpand = !isExpand
                },
                modifier = Modifier
                    .width(24.dp)
                    .height(14.dp),
            ) {

                val drawableResId = remember(isExpand) {
                    if (isExpand) {
                        R.drawable.ic_down_arrow
                    } else R.drawable.ic_right_arrow
                }

                Icon(
                    imageVector = ImageVector
                        .vectorResource(drawableResId),
                    contentDescription = null,
                    tint = Orange80,
                    modifier = Modifier
                        .width(14.dp)
                        .height(14.dp)
                )
            }
        }

        if (isExpand) {
            Spacer(Modifier.height(16.dp))

            SecondaryDescription(
                title = stringResource(R.string.jumlah_buah),
                description = stringResource(R.string.buah, detectedCocoaCount)
            )

            Spacer(Modifier.height(24.dp))

            SecondaryDescription(
                title = stringResource(R.string.bobot_buah),
                description = stringResource(R.string.kg_assumed_weight, cocoaAverageWeightInputProvider())
            )

            Spacer(Modifier.height(24.dp))


            groupedDamagedLevelToDetectedCocoa.keys.forEachIndexed{ index, damageLevel ->
                key(damageLevel) {
                    PriceAnalysisDetails(
                        onDetectedCacaoImageClicked = onDetectedCacaoImageClicked,
                        detectedCocoas = groupedDamagedLevelToDetectedCocoa[damageLevel] ?: persistentListOf(),
                        damageLevel = damageLevel
                    )

                    if(index != groupedDamagedLevelToDetectedCocoa.keys.size - 1){
                        Spacer(Modifier.height(16.dp))
                    }

                }
            }

        }

        Spacer(Modifier.height(16.dp))

        Row(
            Modifier
                .fillMaxWidth()
                .background(color = Yellow90, shape = RoundedCornerShape(8.dp))
                .dashedBorder(color = Orange80, shape = RoundedCornerShape(8.dp))
                .padding(vertical = 12.dp, horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                stringResource(R.string.sub_total_harga_jual),
                style = MaterialTheme.typography.titleMedium,
                color = Orange90,
                modifier = Modifier
                    .weight(1f)
            )

            Text(
                subTotalPrice,
                style = MaterialTheme.typography.titleMedium,
                color = Black10
            )
        }
    }

}


@Composable
fun PriceAnalysisContentLoading(modifier: Modifier = Modifier) {

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
            .background(color = Color.White, shape = RoundedCornerShape(8.dp))
            .padding(16.dp),
    ) {


        TitleShimmeringLoading(
            height = 17.dp,
            widthRatio = 0.3f,
        )

        Spacer(Modifier.height(8.dp))

        DescriptionShimmeringLoading(
            lineHeight = 17.dp,
            lastLineWidthRatio = 1f,
            lineCount = 1
        )

        Spacer(Modifier.height(24.dp))

        TitleShimmeringLoading(
            height = 17.dp,
            widthRatio = 0.2f,
        )

        Spacer(Modifier.height(8.dp))

        DescriptionShimmeringLoading(
            lineHeight = 17.dp,
            lastLineWidthRatio = 1f,
            lineCount = 1
        )

        Spacer(Modifier.height(24.dp))

        TitleShimmeringLoading(
            height = 17.dp,
            widthRatio = 0.3f,
        )

        Spacer(Modifier.height(8.dp))

        DescriptionShimmeringLoading(
            lineHeight = 17.dp,
            lastLineWidthRatio = 0.7f,
            lineCount = 3
        )

        Spacer(Modifier.height(24.dp))

        TitleShimmeringLoading(
            height = 17.dp,
            widthRatio = 0.5f,
        )

        Spacer(Modifier.height(8.dp))

        DescriptionShimmeringLoading(
            lineHeight = 17.dp,
            lastLineWidthRatio = 0.7f,
            lineCount = 2
        )

        Spacer(Modifier.height(24.dp))

        TitleShimmeringLoading(
            height = 17.dp,
            widthRatio = 0.6f,
        )

        Spacer(Modifier.height(8.dp))

        DescriptionShimmeringLoading(
            lineHeight = 17.dp,
            lastLineWidthRatio = 1f,
            lineCount = 1
        )


    }

}

@Preview(showBackground = true, heightDp = 1500)
@Composable
private fun PriceAnalysisContentPreview() {
    KakaoXpertTheme {
        PriceAnalysisContent(
            diseaseName = "Busuk Buah",
            isInitiallyExpanded = true
        )
    }
}

