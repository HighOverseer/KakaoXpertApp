package com.neotelemetrixgdscunand.kakaoxpert.domain.presentation

import com.neotelemetrixgdscunand.kakaoxpert.domain.model.BoundingBox

interface CocoaDamageLevelPredictionHelper {

    suspend fun setup()

    suspend fun predict(
        imagePath: String,
        boundingBoxes: List<BoundingBox>
    ): CocoaDamageLevelPredictionResult

    fun cleanResource()
}