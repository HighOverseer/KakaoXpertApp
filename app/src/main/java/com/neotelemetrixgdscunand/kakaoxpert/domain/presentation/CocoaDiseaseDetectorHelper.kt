package com.neotelemetrixgdscunand.kakaoxpert.domain.presentation


interface CocoaDiseaseDetectorHelper {

    suspend fun setupImageDetector()

    suspend fun detect(imagePath: String): ImageDetectorResult

    fun clearResource()

    companion object {
        val TEMP_CLASSES = List(100) { "class ${it + 1}" }

        const val INPUT_MEAN = 0f
        const val INPUT_STANDARD_DEVIATION = 255f
        const val CONFIDENCE_THRESHOLD = 0.1f
        const val IOU_THRESHOLD = 0.5f
    }
}

