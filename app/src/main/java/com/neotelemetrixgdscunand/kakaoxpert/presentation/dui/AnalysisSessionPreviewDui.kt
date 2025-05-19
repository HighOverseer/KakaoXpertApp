package com.neotelemetrixgdscunand.kakaoxpert.presentation.dui

data class AnalysisSessionPreviewDui(
    val id: Int,
    val title: String,
    val imageUrlOrPath: String,
    val date: String,
    val predictedPrice: Float,
    val hasSynced: Boolean,
    val availableOffline:Boolean
)