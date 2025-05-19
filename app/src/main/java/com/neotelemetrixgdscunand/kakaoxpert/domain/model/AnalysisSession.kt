package com.neotelemetrixgdscunand.kakaoxpert.domain.model

import kotlin.random.Random

data class AnalysisSession(
    val id: Int = Random.nextInt(0, 1_000_000),
    val title: String = "",
    val imageUrlOrPath: String = "",
    val createdAt: Long = 0,
    val predictedPrice: Float = 0f,
    val detectedCocoas: List<DetectedCocoa> = emptyList(),
    val solutionEn: String? = null,
    val preventionsEn: String? = null,
    val solutionId: String? = null,
    val preventionsId: String? = null
)

