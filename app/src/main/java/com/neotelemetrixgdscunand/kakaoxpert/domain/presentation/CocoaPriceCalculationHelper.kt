package com.neotelemetrixgdscunand.kakaoxpert.domain.presentation

import com.neotelemetrixgdscunand.kakaoxpert.domain.model.BoundingBox

interface CocoaPriceCalculationHelper {

    suspend fun calculate(boundingBoxAndDamageLevelList:List<Pair<BoundingBox,Float>>):List<Float>
}