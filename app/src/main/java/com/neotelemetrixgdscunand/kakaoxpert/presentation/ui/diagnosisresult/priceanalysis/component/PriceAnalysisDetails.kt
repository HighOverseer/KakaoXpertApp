package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.diagnosisresult.priceanalysis.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.unit.sp
import com.neotelemetrixgdscunand.kakaoxpert.R
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.CocoaDisease
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.DamageLevelCategory
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.DamageLevelSubCategory
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.DetectedCocoa
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Black10
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Green55
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Green60
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Grey60
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Grey90
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.KakaoXpertTheme
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Orange80
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Yellow90
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.diagnosisresult.component.PrimaryDescription
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.diagnosisresult.diseasediagnosis.compoenent.DetectedCacaoImageGrid
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableMap

@Composable
fun PriceAnalysisDetails(
    modifier: Modifier = Modifier,
    isInitiallyExpanded: Boolean = false,
    groupedDetectedDisease: ImmutableMap<CocoaDisease, ImmutableList<DetectedCocoa>> =
        emptyMap<CocoaDisease, ImmutableList<DetectedCocoa>>().toImmutableMap(),
    subDamageLevelSubCategory: DamageLevelSubCategory = DamageLevelCategory.Low.secondSubLevelCategory,
    onDetectedCacaoImageClicked: (Int) -> Unit = { }
) {

    var isDetailsExpanded by remember(isInitiallyExpanded) {
        mutableStateOf(isInitiallyExpanded)
    }
    val groupedDetectedDiseaseKeys = remember(groupedDetectedDisease) {
        groupedDetectedDisease.keys.toList()
    }

    Column(
        modifier = modifier
            .background(color = Color.White, shape = RoundedCornerShape(8.dp))
            .border(shape = RoundedCornerShape(8.dp), color = Green55, width = 1.dp)
            .padding(vertical = 16.dp, horizontal = 8.dp),
    ) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(
                stringResource(subDamageLevelSubCategory.subTitleResId),
                style = MaterialTheme.typography.titleMedium.copy(fontSize = 14.sp),
                color = Green60,
                modifier = Modifier.weight(1f)
            )

            IconButton(
                onClick = {
                    isDetailsExpanded = !isDetailsExpanded
                },
                modifier = Modifier
                    .width(24.dp)
                    .height(14.dp),
            ) {

                val drawableResId = remember(isDetailsExpanded) {
                    if (isDetailsExpanded) {
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

        if (isDetailsExpanded) {
            Spacer(Modifier.height(16.dp))

            DetectedCacaoImageGrid(
                detectedCacaos = groupedDetectedDiseaseKeys.firstOrNull()?.let {
                    val a = groupedDetectedDisease[it]
                    a
                } ?: persistentListOf(),
                onItemClicked = onDetectedCacaoImageClicked
            )

            Spacer(Modifier.height(24.dp))

            PrimaryDescription(
                titleTextColor = Green60,
                descriptionTextColor = Black10,
                title = stringResource(R.string.sub_harga_satuan),
                description = "Rp 3.000 - Rp 6.000/kg."
            )

            Spacer(Modifier.height(24.dp))

            PrimaryDescription(
                titleTextColor = Green60,
                descriptionTextColor = Black10,
                title = stringResource(R.string.sub_total_harga),
                description = "Rp 3.000 - Rp 6.000/kg."
            )
        }

    }
}

@Preview
@Composable
private fun PriceAnalysisDetailsPreview() {
    KakaoXpertTheme {
        PriceAnalysisDetails()
    }
}