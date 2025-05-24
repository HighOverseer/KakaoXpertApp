package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.analysissessionresult.diseasediagnosis.component

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.neotelemetrixgdscunand.kakaoxpert.R
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.CocoaDisease
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.DetectedCocoa
import com.neotelemetrixgdscunand.kakaoxpert.presentation.mapper.CocoaDiseaseMapper
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Black10
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Green55
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.KakaoXpertTheme
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Orange80
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Orange90
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Yellow90
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.analysissessionresult.component.SecondaryDescription
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.auth.component.PrimaryTextField
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.util.ImagePainterStable
import com.neotelemetrixgdscunand.kakaoxpert.presentation.utils.dashedBorder
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableMap

@Composable
fun DetectedCacaoDiseaseOverviewSection(
    modifier: Modifier = Modifier,
    groupedDetectedDisease: ImmutableMap<CocoaDisease, ImmutableList<DetectedCocoa>> =
        mutableMapOf<CocoaDisease, ImmutableList<DetectedCocoa>>().toImmutableMap(),
    navigateToCacaoImageDetail: (Int) -> Unit = { }
) {

    val groupedDetectedDiseaseKeys = remember(groupedDetectedDisease) {
        groupedDetectedDisease.keys
    }

    val detectedCocoaCount = remember(groupedDetectedDisease) {
        groupedDetectedDisease.values.sumOf { it.size }
    }

    val detectedDiseaseCount = remember(groupedDetectedDisease) {
        groupedDetectedDisease.keys.size
    }

    Column(
        modifier
            .fillMaxWidth()
            .background(color = Color.White, shape = RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {
        SecondaryDescription(
            title = stringResource(R.string.total_buah_keseluruhan),
            description = stringResource(R.string.buah, detectedCocoaCount)
        )

        Spacer(Modifier.height(16.dp))

        SecondaryDescription(
            title = stringResource(R.string.total_penyakit_yang_terdeteksi),
            description = stringResource(R.string.penyakit_format, detectedDiseaseCount)
        )

        Spacer(Modifier.height(16.dp))


        Card(
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.Transparent
            ),
            modifier = Modifier
                .dashedBorder(color = Orange80, shape = RoundedCornerShape(8.dp))
        ){
            Column(modifier = Modifier
                .fillMaxWidth()
                .background(color = Yellow90, RoundedCornerShape(8.dp))
                .padding(top = 16.dp, start = 16.dp, end = 16.dp)) {

                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    ImagePainterStable(
                        modifier = Modifier
                            .rotate(90f)
                            .size(18.dp),
                        drawableResId = R.drawable.ic_search_analysis,
                        contentScale = ContentScale.Fit,
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(Orange80)
                    )

                    Spacer(Modifier.width(4.dp))

                    Text(
                        stringResource(R.string.penyakit_hama_yang_terdeteksi),
                        style = MaterialTheme.typography.titleMedium,
                        color = Orange80
                    )

                }

                Spacer(Modifier.height(16.dp))

                groupedDetectedDiseaseKeys.forEachIndexed { index, diseaseKey ->
                    key(diseaseKey) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
//                            val bulletNumberModifier = remember {
//                                Modifier
//                                    .wrapContentSize()
//                                    .clip(CircleShape)
//                                    .background(color = Orange90)
//                                    .sizeIn(minWidth = 18.dp, minHeight = 18.dp)
//                            }
//
//                            Box(
//                                contentAlignment = Alignment.Center,
//                                modifier = bulletNumberModifier
//                            ) {
//                                Text(
//                                    "${index + 1}",
//                                    style = MaterialTheme.typography.labelMedium.copy(
//                                        fontSize = 12.sp
//                                    ),
//                                    color = Color.White
//                                )
//                            }
//

                            Text(
                                text = "${ index.plus(1) }.",
                                style = MaterialTheme.typography.titleLarge,
                                color = Black10
                            )

                            Spacer(Modifier.width(2.dp))

                            val diseaseNameResId = CocoaDiseaseMapper.mapToNameResId[diseaseKey]
                            val diseaseName = diseaseNameResId?.let { stringResource(it) } ?: "-"
                            Text(
                                text = diseaseName,
                                style = MaterialTheme.typography.titleLarge,
                                color = Black10
                            )
                        }

                        Spacer(Modifier.height(12.dp))

                        DetectedCacaoImageGrid(
                            detectedCacaos = groupedDetectedDisease[diseaseKey] ?: persistentListOf(),
                            onItemClicked = navigateToCacaoImageDetail,
                            textStyle = MaterialTheme.typography.titleMedium,
                            textColor = Orange90
                        )
                        Spacer(Modifier.height(8.dp))
                    }

                }
            }
        }
        
    }

}

@Preview
@Composable
private fun DetectedCacaoDiseaseOverviewSectionPreview() {
    KakaoXpertTheme {
        DetectedCacaoDiseaseOverviewSection()
    }
    
}
        