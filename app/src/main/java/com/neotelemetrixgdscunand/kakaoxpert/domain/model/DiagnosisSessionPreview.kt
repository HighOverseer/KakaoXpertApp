package com.neotelemetrixgdscunand.kamekapp.domain.model

data class DiagnosisSessionPreview(
    val id: Int,
    val title: String,
    val imageUrlOrPath: String,
    val date: String,
    val predictedPrice: Float,
)