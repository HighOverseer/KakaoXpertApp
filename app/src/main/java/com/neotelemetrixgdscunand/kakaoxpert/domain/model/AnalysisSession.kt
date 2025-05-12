package com.neotelemetrixgdscunand.kakaoxpert.domain.model

import kotlin.random.Random

data class AnalysisSession(
    val id: Int = Random.nextInt(0, 1_000_000),
    val title: String = "",
    val imageUrlOrPath: String = "",
    val date: String = "",
    val predictedPrice: Float = 0f,
    val detectedCocoas: List<DetectedCocoa> = emptyList()
)
