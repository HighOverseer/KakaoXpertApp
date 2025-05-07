package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.diagnosisresult.diseasediagnosis

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.neotelemetrixgdscunand.kakaoxpert.R
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.diagnosisresult.diseasediagnosis.compoenent.DetectedCacaoDiseasePreviewSection
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.diagnosisresult.diseasediagnosis.compoenent.DiagnosisBottomContent
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.diagnosisresult.diseasediagnosis.compoenent.DiagnosisDiseaseDetails
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.diagnosisresult.util.DiagnosisSessionComposeStable
import com.neotelemetrixgdscunand.kamekapp.domain.model.CacaoDisease
import com.neotelemetrixgdscunand.kamekapp.domain.model.DetectedCacao
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap

@Composable
fun DiagnosisDiseaseTabScreen(
    modifier: Modifier = Modifier,
    groupedDetectedDisease: ImmutableMap<CacaoDisease, ImmutableList<DetectedCacao>> =
        mutableMapOf<CacaoDisease, ImmutableList<DetectedCacao>>().toImmutableMap(),
    navigateToCacaoImageDetail: (Int) -> Unit = { },
    diagnosisSessionComposeStable: DiagnosisSessionComposeStable = DiagnosisSessionComposeStable(),
    isLoadingProvider: () -> Boolean = { false },
    isItemExpandProvider: (Int) -> Boolean = { false },
    toggleItemExpand: (Int) -> Unit = { },
) {
    DetectedCacaoDiseasePreviewSection(
        modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .background(
                color = Color.White,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(top = 16.dp, start = 16.dp, end = 16.dp),
        groupedDetectedDisease = groupedDetectedDisease,
        navigateToCacaoImageDetail = navigateToCacaoImageDetail
    )

    Spacer(Modifier.height(16.dp))

    val context = LocalContext.current
    val preventions = remember(diagnosisSessionComposeStable) {
        diagnosisSessionComposeStable.detectedCacaos.firstOrNull()?.disease?.controlStringResId?.map {
            context.getString(it)
        }?.toImmutableList() ?: persistentListOf()
    }
    val solutionStringResId = remember(diagnosisSessionComposeStable) {
        diagnosisSessionComposeStable
            .detectedCacaos
            .firstOrNull()
            ?.disease
            ?.solutionStringResId
    }

    DiagnosisBottomContent(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
            .background(color = Color.White, shape = RoundedCornerShape(8.dp))
            .padding(16.dp),
        preventions = preventions,
        solution = solutionStringResId?.let { stringResource(it) } ?: "",
        isLoadingProvider = isLoadingProvider
    )

    Text(
        modifier = Modifier.padding(horizontal = 16.dp),
        text = stringResource(R.string.rincian_penyakit),
        style = MaterialTheme.typography.titleMedium
    )

    Spacer(Modifier.height(16.dp))

    DiagnosisDiseaseDetails(
        groupedDetectedDisease = groupedDetectedDisease,
        isItemExpandProvider = isItemExpandProvider,
        toggleItemExpand = toggleItemExpand,
        navigateToCacaoImageDetail = navigateToCacaoImageDetail,
        isLoadingProvider = isLoadingProvider
    )
}