package com.neotelemetrixgdscunand.kakaoxpert.presentation.dui

import com.neotelemetrixgdscunand.kakaoxpert.domain.model.DetectedCocoa
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlin.random.Random

data class DiagnosisSessionDui(
    val id: Int = Random.nextInt(0, 1_000_000),
    val title: String = "",
    val imageUrlOrPath: String = "",
    val date: String = "",
    val predictedPrice: Float = 0f,
    val detectedCocoas: ImmutableList<DetectedCocoa> = persistentListOf()
)