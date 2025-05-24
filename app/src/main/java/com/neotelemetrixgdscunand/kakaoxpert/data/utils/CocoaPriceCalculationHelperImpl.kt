package com.neotelemetrixgdscunand.kakaoxpert.data.utils

import com.neotelemetrixgdscunand.kakaoxpert.domain.data.CocoaPriceInfoRepository
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.BoundingBox
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.CocoaDisease
import com.neotelemetrixgdscunand.kakaoxpert.domain.presentation.CocoaPriceCalculationHelper
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.analysissessionresult.util.roundOffDecimal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CocoaPriceCalculationHelperImpl @Inject constructor(
    private val cocoaPriceInfoRepository: CocoaPriceInfoRepository
):CocoaPriceCalculationHelper {

    override suspend fun calculate(boundingBoxAndDamageLevelList: List<Pair<BoundingBox, Float>>): List<Float>
    = withContext(Dispatchers.Default){

        val cocoaDiseasePriceInfoList = cocoaPriceInfoRepository.getCocoaDiseasePriceInfoList()
            .distinctBy { it.disease }

        val mapCocoaDiseaseToPriceInfo = cocoaDiseasePriceInfoList.associateBy { it.disease }

        val healthyCocoaPrice = cocoaDiseasePriceInfoList.first { it.disease == CocoaDisease.NONE }
            .highestPrice

        val cocoaSellPriceListDeferred = boundingBoxAndDamageLevelList.map { (boundingBox, damageLevel) -> async {
                val currentBoundingBoxCocoaDisease = CocoaDisease.getDiseaseFromName(boundingBox.label)
                val decreasingPricePerOneDamageLevel = mapCocoaDiseaseToPriceInfo[currentBoundingBoxCocoaDisease]
                    ?.decreasingRatePerDamageLevel ?: 0f

                val decreasingAmount = damageLevel * decreasingPricePerOneDamageLevel

                val cocoaSellPrice = (healthyCocoaPrice - decreasingAmount)
                    .coerceIn(0f, healthyCocoaPrice)
                    .roundOffDecimal(n = 2)

                cocoaSellPrice
            }
        }

        val cocoaSellPriceList = cocoaSellPriceListDeferred.awaitAll()
        cocoaSellPriceList
    }
}