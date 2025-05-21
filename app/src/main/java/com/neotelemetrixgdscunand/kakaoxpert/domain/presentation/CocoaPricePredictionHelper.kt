package com.neotelemetrixgdscunand.kakaoxpert.domain.presentation

import com.neotelemetrixgdscunand.kakaoxpert.domain.model.BoundingBox

interface CocoaPricePredictionHelper {

    suspend fun setup()

    suspend fun predict(imagePath: String, boundingBoxes: List<BoundingBox>): CocoaPredictionResult

    fun cleanResource()
}