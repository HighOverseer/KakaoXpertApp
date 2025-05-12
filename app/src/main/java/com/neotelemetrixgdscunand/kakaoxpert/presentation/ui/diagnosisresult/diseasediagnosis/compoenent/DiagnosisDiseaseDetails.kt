package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.diagnosisresult.diseasediagnosis.compoenent

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
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.neotelemetrixgdscunand.kakaoxpert.R
import com.neotelemetrixgdscunand.kakaoxpert.presentation.mapper.CocoaDiseaseMapper
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.KakaoXpertTheme
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Orange80
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.diagnosisresult.component.PrimaryDescription
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.diagnosisresult.component.SecondaryDescription
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.diagnosisresult.component.TitleShimmeringLoading
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.CocoaDisease
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.DetectedCocoa
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableMap

@Composable
fun DiagnosisDiseaseDetails(
    modifier: Modifier = Modifier,
    groupedDetectedDisease: ImmutableMap<CocoaDisease, ImmutableList<DetectedCocoa>> =
        emptyMap<CocoaDisease, ImmutableList<DetectedCocoa>>().toImmutableMap(),
    isItemExpandProvider: (index: Int) -> Boolean = { false },
    toggleItemExpand: (index: Int) -> Unit = { },
    navigateToCacaoImageDetail: (Int) -> Unit = { },
    isLoadingProvider: () -> Boolean = { false }
) {
    val groupedDetectedDiseaseKeys = remember(groupedDetectedDisease) {
        groupedDetectedDisease.keys
    }
    if (isLoadingProvider()) {
        DiagnosisDiseaseDetailsLoading()

    } else groupedDetectedDiseaseKeys.forEachIndexed { index, diseaseKey ->
        key(diseaseKey) {
            val diseaseName = CocoaDiseaseMapper.mapToNameResId[diseaseKey]?.let{ stringResource(it) } ?: "-"
            val diseaseCause = CocoaDiseaseMapper.mapToCauseResId[diseaseKey]?.let{ stringResource(it) } ?: "-"
            val diseaseSymptoms = CocoaDiseaseMapper.mapToSymptomResId[diseaseKey]?.let{ stringResource(it) } ?: "-"
            val diseaseSeedCondition = CocoaDiseaseMapper.mapToSeedConditionResId[diseaseKey]?.let{ stringResource(it) } ?: "-"

            DiagnosisDiseaseDetailItem(
                isExpandProvider = { isItemExpandProvider(index) },
                toggleExpand = { toggleItemExpand(index) },
                modifier = modifier.padding(bottom = 8.dp),
                diseaseName = diseaseName,
                detectedCacaos = groupedDetectedDisease[diseaseKey]
                    ?: persistentListOf(),
                diseaseCause = diseaseCause,
                diseaseSymptoms = diseaseSymptoms,
                seedCondition = diseaseSeedCondition,
                onDetectedCacaoImageClicked = navigateToCacaoImageDetail
            )
        }
    }
}

@Composable
fun DiagnosisDiseaseDetailItem(
    modifier: Modifier = Modifier,
    diseaseName: String = "-",
    diseaseCause: String = "-",
    diseaseSymptoms: String = "-",
    seedCondition: String = "-",
    detectedCacaos: ImmutableList<DetectedCocoa> = persistentListOf(),
    onDetectedCacaoImageClicked: (Int) -> Unit = { },
    isExpandProvider: () -> Boolean = { false },
    toggleExpand: () -> Unit = { }
) {
    val isExpand = isExpandProvider()
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp)
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
                onClick = toggleExpand,
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
                    tint = Color.Black,
                    modifier = Modifier
                        .width(14.dp)
                        .height(14.dp)
                )
            }
        }

        if (isExpand) {
            Spacer(Modifier.height(24.dp))

            DetectedCacaoImageGrid(
                detectedCacaos = detectedCacaos,
                onItemClicked = onDetectedCacaoImageClicked
            )

            Spacer(Modifier.height(16.dp))


            PrimaryDescription(
                title = stringResource(R.string.penyebab),
                description = diseaseCause,
                descriptionTextAlign = TextAlign.Justify
            )

            Spacer(Modifier.height(24.dp))

            PrimaryDescription(
                title = stringResource(R.string.gejala),
                description = diseaseSymptoms,
                descriptionTextAlign = TextAlign.Justify
            )

            Spacer(Modifier.height(24.dp))

            SecondaryDescription(
                title = stringResource(R.string.kondisi_biji),
                description = seedCondition,
                descriptionTextAlign = TextAlign.Justify
            )
        }
    }

}

@Composable
fun DiagnosisDiseaseDetailsLoading(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp, bottom = 16.dp, end = 16.dp)
            .background(color = Color.White, shape = RoundedCornerShape(8.dp))
            .padding(16.dp),
    ) {

        TitleShimmeringLoading(
            height = 17.dp,
            widthRatio = 0.3f
        )

        Spacer(Modifier.height(8.dp))

        DescriptionShimmeringLoading(
            lineHeight = 17.dp,
            lineCount = 2,
            lastLineWidthRatio = 0.7f
        )

        Spacer(Modifier.height(24.dp))

        TitleShimmeringLoading(
            height = 17.dp,
            widthRatio = 0.3f
        )

        Spacer(Modifier.height(8.dp))

        DescriptionShimmeringLoading(
            lineHeight = 17.dp,
            lineCount = 3,
            lastLineWidthRatio = 0.7f
        )

        Spacer(Modifier.height(24.dp))

        TitleShimmeringLoading(
            height = 17.dp,
            widthRatio = 0.4f
        )

        Spacer(Modifier.height(8.dp))

        DescriptionShimmeringLoading(
            lineHeight = 17.dp,
            lineCount = 5,
            lastLineWidthRatio = 0.7f
        )
    }
}

@Preview
@Composable
private fun DiagnosisDiseaseDetailsPreview() {
    KakaoXpertTheme {
        DiagnosisDiseaseDetails()
    }

}