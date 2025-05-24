package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.diagnosisresult.priceanalysis

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.neotelemetrixgdscunand.kakaoxpert.R
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.CocoaDisease
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.DamageLevelCategory
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.DetectedCocoa
import com.neotelemetrixgdscunand.kakaoxpert.presentation.dui.PriceAnalysisOverviewDui
import com.neotelemetrixgdscunand.kakaoxpert.presentation.mapper.CocoaDiseaseMapper
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Black10
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.KakaoXpertTheme
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.diagnosisresult.priceanalysis.component.PriceAnalysisContent
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.diagnosisresult.priceanalysis.component.PriceAnalysisInformationPreviewSection
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.diagnosisresult.priceanalysis.component.PriceAnalysisOverviewSection
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap
import kotlin.math.roundToInt

@Composable
fun PriceAnalysisTabScreen(
    modifier: Modifier = Modifier,
    isLoadingProvider: () -> Boolean = { false },
    detectedCocoas:ImmutableList<DetectedCocoa> = persistentListOf(),
    priceAnalysisOverviewDui: PriceAnalysisOverviewDui = PriceAnalysisOverviewDui(),
    navigateToCacaoImageDetail: (Int) -> Unit = { },
    showSnackbar: (String) -> Unit = { }
) {
    if (isLoadingProvider()) {
        PriceAnalysisContent()
    } else {

        PriceAnalysisInformationPreviewSection(
            showSnackbar = showSnackbar
        )

        Spacer(Modifier.height(16.dp))

        PriceAnalysisOverviewSection(
            priceAnalysisOverviewDui = priceAnalysisOverviewDui
        )

        Spacer(Modifier.height(16.dp))

        Text(
            stringResource(R.string.rincian_prediksi_harga),
            style = MaterialTheme.typography.titleMedium,
            color = Black10,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )

        Spacer(Modifier.height(16.dp))

        val outermostPaddingModifier = remember {
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        }

//        val damageLevelCategoryInfo = remember {
//            listOf(
//                DamageLevelCategory.LOW,
//                DamageLevelCategory.MEDIUM,
//                DamageLevelCategory.HIGH
//            )
//        }

        val doubleGroupedData = remember(detectedCocoas){
            val outerMap = mutableMapOf<CocoaDisease, ImmutableMap<Int, ImmutableList<DetectedCocoa>>>()
            detectedCocoas.groupBy { it.disease }.map {
                val (cocoaDisease, list) = it.toPair()
                val immutableList = list.toImmutableList()

                val innerMap = mutableMapOf<Int, ImmutableList<DetectedCocoa>>()
                immutableList.groupBy { item ->
                    item.damageLevel.roundToInt()
                }.map { innerItem ->
                    val (damageLevel, innerList) = innerItem.toPair()
                    innerMap[damageLevel] = innerList.toImmutableList()
                }
                outerMap[cocoaDisease] = innerMap.toImmutableMap()
            }
            outerMap.toImmutableMap()
        }

        val groupedDetectedDiseaseKeys = remember(doubleGroupedData) {
            doubleGroupedData.keys
        }

        val emptyMap = remember {
            emptyMap<Int, ImmutableList<DetectedCocoa>>()
                .toImmutableMap()
        }


        groupedDetectedDiseaseKeys.forEachIndexed{index, diseaseKey ->
            key(diseaseKey) {
                val isInitiallyExpanded = index == 0
                
                val diseaseName =
                    CocoaDiseaseMapper.mapToNameResId[diseaseKey]?.let { stringResource(it) } ?: "-"

                PriceAnalysisContent(
                    modifier = outermostPaddingModifier,
                    isInitiallyExpanded = isInitiallyExpanded,
                    //damageLevelCategory = damageLevelCategoryInfo[index],
                    groupedDamagedLevelToDetectedCocoa = doubleGroupedData[diseaseKey] ?: emptyMap,
                    onDetectedCacaoImageClicked = navigateToCacaoImageDetail,
                    diseaseName = diseaseName
                )
                Spacer(Modifier.height(16.dp))
            }
        }
    }

}

@Preview
@Composable
private fun PriceAnalysisTabScreenPreview() {
    KakaoXpertTheme {
        PriceAnalysisTabScreen(
            isLoadingProvider = { false }
        )
    }
}