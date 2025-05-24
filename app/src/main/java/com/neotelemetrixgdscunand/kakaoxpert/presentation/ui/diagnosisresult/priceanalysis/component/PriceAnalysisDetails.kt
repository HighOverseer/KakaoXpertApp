package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.diagnosisresult.priceanalysis.component

import androidx.compose.animation.animateContentSize
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.neotelemetrixgdscunand.kakaoxpert.R
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.DetectedCocoa
import com.neotelemetrixgdscunand.kakaoxpert.presentation.mapper.CocoaDamageLevelMapper
import com.neotelemetrixgdscunand.kakaoxpert.presentation.mapper.DuiMapper.formatToIdrCurrency
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Black10
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Green55
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Green60
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.KakaoXpertTheme
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Orange80
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Orange90
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Yellow90
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.diagnosisresult.component.PrimaryDescription
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.diagnosisresult.diseasediagnosis.compoenent.DetectedCacaoImageGrid
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.util.formatFloat
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlin.math.roundToInt

@Composable
fun PriceAnalysisDetails(
    modifier: Modifier = Modifier,
    isInitiallyExpanded: Boolean = true,
    damageLevel:Int = 0,
    detectedCocoas:ImmutableList<DetectedCocoa> = persistentListOf(),
    //subDamageLevelSubCategory: DamageLevelSubCategory = DamageLevelCategory.Low.secondSubLevelCategory,
    onDetectedCacaoImageClicked: (Int) -> Unit = { }
) {

    var isDetailsExpanded by remember(isInitiallyExpanded) {
        mutableStateOf(isInitiallyExpanded)
    }
    val damageLevelDescription = remember(damageLevel){
        CocoaDamageLevelMapper.getFormattedDamageLevelDescription(damageLevel = damageLevel.toFloat())
    }
    val sellPricePerUnit = remember(detectedCocoas) {
        (detectedCocoas.firstOrNull()?.predictedPriceInIdr ?: 0f)
            .formatToIdrCurrency()
    }

    val subTotalPriceUnit = remember(detectedCocoas) {
        detectedCocoas.sumOf { it.predictedPriceInIdr.toDouble() }
            .formatToIdrCurrency()
    }

    Card(
        modifier = Modifier
            .animateContentSize(),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        shape = RoundedCornerShape(8.dp),
        onClick = {
            isDetailsExpanded = !isDetailsExpanded
        }
    ){
        Column(
            modifier = modifier
                .background(color = Yellow90, shape = RoundedCornerShape(8.dp))
                //   .border(shape = RoundedCornerShape(8.dp), color = Green55, width = 1.dp)
                .padding(vertical = 16.dp, horizontal = 8.dp),
        ) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text(
                    damageLevelDescription.getValue(),
                    style = MaterialTheme.typography.titleMedium.copy(fontSize = 14.sp),
                    color = Orange90,
                    modifier = Modifier.weight(1f)
                )

                val drawableResId = remember(isDetailsExpanded) {
                    if (isDetailsExpanded) {
                        R.drawable.ic_down_arrow
                    } else R.drawable.ic_right_arrow
                }

                Icon(
                    imageVector = ImageVector
                        .vectorResource(drawableResId),
                    contentDescription = null,
                    tint = Orange90,
                    modifier = Modifier
                        .padding(horizontal = 5.dp)
                        .width(14.dp)
                        .height(14.dp)
                )
            }

            if (isDetailsExpanded) {
                Spacer(Modifier.height(16.dp))

                DetectedCacaoImageGrid(
                    detectedCacaos = detectedCocoas,
                    onItemClicked = onDetectedCacaoImageClicked
                )

                Spacer(Modifier.height(8.dp))

                Row(Modifier.fillMaxWidth()){
                    Column(Modifier.weight(1f)){
                        Text(
                            modifier = modifier,
                            text = stringResource(R.string.sub_harga_satuan),
                            style = MaterialTheme.typography.titleMedium,
                            color = Orange90
                        )

                        Spacer(Modifier.height(8.dp))

                        Text(
                            sellPricePerUnit,
                            style = MaterialTheme.typography.labelMedium,
                            color = Black10,
                            textAlign = TextAlign.Start
                        )
                    }


                    Spacer(Modifier.height(24.dp))

                    Column(Modifier.weight(1f)){
                        Text(
                            modifier = modifier,
                            text = stringResource(R.string.sub_total_harga),
                            style = MaterialTheme.typography.titleMedium,
                            color = Orange90
                        )

                        Spacer(Modifier.height(8.dp))

                        Text(
                            subTotalPriceUnit,
                            style = MaterialTheme.typography.titleMedium,
                            color = Black10,
                            textAlign = TextAlign.Start
                        )
                    }
                }

            }

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