package com.neotelemetrixgdscunand.kakaoxpert.domain.presentation


interface ModelLabelExtractor {

    fun extractNamesFromMetadata(modelPath: String): List<String>

    fun extractNamesFromLabelFile(labelPath: String): List<String>
}