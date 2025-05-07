package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.diagnosisresult.diseasediagnosis.compoenent

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import com.neotelemetrixgdscunand.kakaoxpert.R
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Black10
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Green55
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Orange90
import com.neotelemetrixgdscunand.kamekapp.domain.model.CacaoDisease
import com.neotelemetrixgdscunand.kamekapp.domain.model.DetectedCacao
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableMap

@Composable
fun DetectedCacaoDiseasePreviewSection(
    modifier: Modifier = Modifier,
    groupedDetectedDisease: ImmutableMap<CacaoDisease, ImmutableList<DetectedCacao>> =
        mutableMapOf<CacaoDisease, ImmutableList<DetectedCacao>>().toImmutableMap(),
    navigateToCacaoImageDetail: (Int) -> Unit = { }
) {

    val groupedDetectedDiseaseKeys = remember(groupedDetectedDisease) {
        groupedDetectedDisease.keys
    }

    Column(modifier = modifier) {
        Text(
            stringResource(R.string.penyakit_hama_yang_terdeteksi),
            style = MaterialTheme.typography.titleMedium,
            color = Black10
        )
        Spacer(Modifier.height(16.dp))

        groupedDetectedDiseaseKeys.forEachIndexed { index, diseaseKey ->
            key(diseaseKey) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val bulletNumberModifier = remember {
                        Modifier
                            .wrapContentSize()
                            .clip(CircleShape)
                            .background(color = Green55)
                            .sizeIn(minWidth = 24.dp, minHeight = 24.dp)
                    }

                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = bulletNumberModifier
                    ) {
                        Text(
                            "${index + 1}",
                            style = MaterialTheme.typography.labelMedium,
                            color = Color.White
                        )
                    }

                    Spacer(Modifier.width(8.dp))

                    Text(
                        text = stringResource(diseaseKey.nameResId),
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontStyle = FontStyle.Italic
                        ),
                        color = Orange90
                    )
                }

                Spacer(Modifier.height(12.dp))

                DetectedCacaoImageGrid(
                    detectedCacaos = groupedDetectedDisease[diseaseKey] ?: persistentListOf(),
                    onItemClicked = navigateToCacaoImageDetail
                )
                Spacer(Modifier.height(8.dp))
            }

        }
    }
}