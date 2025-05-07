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
import androidx.compose.ui.unit.dp
import com.neotelemetrixgdscunand.kakaoxpert.R
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Black10
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.diagnosisresult.priceanalysis.component.PriceAnalysisContent
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.diagnosisresult.priceanalysis.component.PriceAnalysisInformationPreviewSection
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.diagnosisresult.priceanalysis.component.PriceAnalysisOverview
import com.neotelemetrixgdscunand.kamekapp.domain.model.CacaoDisease
import com.neotelemetrixgdscunand.kamekapp.domain.model.DamageLevelCategory
import com.neotelemetrixgdscunand.kamekapp.domain.model.DetectedCacao
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.toImmutableMap

@Composable
fun PriceAnalysisTabScreen(
    modifier: Modifier = Modifier,
    isLoadingProvider: () -> Boolean = { false },
    groupedDetectedDisease: ImmutableMap<CacaoDisease, ImmutableList<DetectedCacao>> =
        emptyMap<CacaoDisease, ImmutableList<DetectedCacao>>().toImmutableMap(),
    navigateToCacaoImageDetail: (Int) -> Unit = { }
) {
    if (isLoadingProvider()) {
        PriceAnalysisContent()
    } else {
        PriceAnalysisInformationPreviewSection()

        Spacer(Modifier.height(16.dp))

        PriceAnalysisOverview()

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

        val damageLevelCategoryInfo = remember {
            listOf(
                DamageLevelCategory.Low,
                DamageLevelCategory.Medium,
                DamageLevelCategory.High
            )
        }

        repeat(3) {
            key(it) {
                PriceAnalysisContent(
                    modifier = outermostPaddingModifier,
                    damageLevelCategory = damageLevelCategoryInfo[it],
                    groupedDetectedDisease = groupedDetectedDisease,
                    onDetectedCacaoImageClicked = navigateToCacaoImageDetail
                )
                Spacer(Modifier.height(16.dp))
            }
        }
    }

}