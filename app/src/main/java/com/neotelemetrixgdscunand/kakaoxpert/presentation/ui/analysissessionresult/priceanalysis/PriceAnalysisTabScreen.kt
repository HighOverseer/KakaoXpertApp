package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.analysissessionresult.priceanalysis

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
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.DetectedCocoa
import com.neotelemetrixgdscunand.kakaoxpert.presentation.dui.PriceAnalysisOverviewDui
import com.neotelemetrixgdscunand.kakaoxpert.presentation.mapper.CocoaDiseaseMapper
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Black10
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.KakaoXpertTheme
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.analysissessionresult.emptyGroupedMap2
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.analysissessionresult.priceanalysis.component.PriceAnalysisContent
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.analysissessionresult.priceanalysis.component.PriceAnalysisContentLoading
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.analysissessionresult.priceanalysis.component.PriceAnalysisInformationPreviewSection
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.analysissessionresult.priceanalysis.component.PriceAnalysisOverviewSection
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.toImmutableMap

@Composable
fun PriceAnalysisTabScreen(
    modifier: Modifier = Modifier,
    isLoadingProvider: () -> Boolean = { false },
    groupedDetectedDiseaseToDamageLevelsToDetectedCocoas: ImmutableMap<CocoaDisease, ImmutableMap<Int, ImmutableList<DetectedCocoa>>> = emptyGroupedMap2,
    priceAnalysisOverviewDui: PriceAnalysisOverviewDui = PriceAnalysisOverviewDui(),
    navigateToCacaoImageDetail: (Int) -> Unit = { },
    showSnackbar: (String) -> Unit = { },
    cocoaAverageWeightInputProvider: () -> String = { "0.2" },
    onCocoaAverageWeightChanged: (String) -> Unit = { }
) {
    if (isLoadingProvider()) {
        PriceAnalysisContentLoading()
    } else {


        PriceAnalysisInformationPreviewSection(
            showSnackbar = showSnackbar
        )

        Spacer(Modifier.height(16.dp))

        PriceAnalysisOverviewSection(
            priceAnalysisOverviewDui = priceAnalysisOverviewDui,
            cocoaAverageWeightProvider = cocoaAverageWeightInputProvider,
            onCocoaAverageWeightChanged = onCocoaAverageWeightChanged,
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

        val groupedDetectedDiseaseKeys =
            remember(groupedDetectedDiseaseToDamageLevelsToDetectedCocoas) {
                groupedDetectedDiseaseToDamageLevelsToDetectedCocoas.keys
            }

        val emptyMap = remember {
            emptyMap<Int, ImmutableList<DetectedCocoa>>()
                .toImmutableMap()
        }


        groupedDetectedDiseaseKeys.forEachIndexed { index, diseaseKey ->
            key(diseaseKey) {
                val isInitiallyExpanded = index == 0

                val diseaseName =
                    CocoaDiseaseMapper.mapToNameResId[diseaseKey]?.let { stringResource(it) } ?: "-"

                PriceAnalysisContent(
                    modifier = outermostPaddingModifier,
                    isInitiallyExpanded = isInitiallyExpanded,
                    //damageLevelCategory = damageLevelCategoryInfo[index],
                    groupedDamagedLevelToDetectedCocoa = groupedDetectedDiseaseToDamageLevelsToDetectedCocoas[diseaseKey]
                        ?: emptyMap,
                    onDetectedCacaoImageClicked = navigateToCacaoImageDetail,
                    diseaseName = diseaseName,
                    cocoaAverageWeightInputProvider = cocoaAverageWeightInputProvider
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