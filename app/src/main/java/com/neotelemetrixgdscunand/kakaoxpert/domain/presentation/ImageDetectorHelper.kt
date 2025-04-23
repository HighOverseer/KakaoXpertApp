package com.neotelemetrixgdscunand.kakaoxpert.domain.presentation

import kotlinx.coroutines.flow.SharedFlow


interface ImageDetectorHelper {

    val result: SharedFlow<ImageDetectorResult>

    suspend fun setupImageDetector()

    suspend fun detect(imagePath: String)

    fun clearResource()

    companion object {
        val TEMP_CLASSES = List(100) { "class ${it + 1}" }

        const val INPUT_MEAN = 0f
        const val INPUT_STANDARD_DEVIATION = 255f
        const val CONFIDENCE_THRESHOLD = 0.1f
        const val IOU_THRESHOLD = 0.5f

        const val MODEL_PATH = "rev_2.tflite"
        const val LABEL_PATH: String = "labels.txt"
    }
}

