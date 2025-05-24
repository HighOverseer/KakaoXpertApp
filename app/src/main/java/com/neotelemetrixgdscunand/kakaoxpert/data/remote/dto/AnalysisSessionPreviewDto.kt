package com.neotelemetrixgdscunand.kakaoxpert.data.remote.dto

import com.google.gson.annotations.SerializedName

data class AnalysisSessionPreviewDto(

    @field:SerializedName("date")
    val date: String? = null,

    @field:SerializedName("session_name")
    val sessionName: String? = null,

    @field:SerializedName("session_image")
    val sessionImage: String? = null,

    @field:SerializedName("session_id")
    val sessionId: Int? = null,

    @field:SerializedName("total_predicted_price")
    val totalPredictedPrice: Float? = null
)
