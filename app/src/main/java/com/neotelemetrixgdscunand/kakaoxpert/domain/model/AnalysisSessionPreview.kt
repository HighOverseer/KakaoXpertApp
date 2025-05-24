package com.neotelemetrixgdscunand.kakaoxpert.domain.model

data class AnalysisSessionPreview(
    val id: Int,
    val title: String,
    val imageUrlOrPath: String,
    val createdAt: Long,
    val totalPredictedPrice: Float,
    val hasSynced: Boolean,
    val isDetailAvailableInLocal: Boolean
)