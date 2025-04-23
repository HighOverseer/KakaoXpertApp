package com.neotelemetrixgdscunand.kakaoxpert.domain.presentation

import com.neotelemetrixgdscunand.kakaoxpert.domain.model.BoundingBox


sealed interface ImageDetectorResult {
    data object NoObjectDetected : ImageDetectorResult
    data class Success(
        val boundingBoxes: List<BoundingBox>
    ) : ImageDetectorResult

    data class Error(
        val exception: Exception
    ) : ImageDetectorResult
}