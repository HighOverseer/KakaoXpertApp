package com.neotelemetrixgdscunand.kakaoxpert.data.remote.dto

import com.google.gson.annotations.SerializedName

data class AnalysisSessionDto(

    @field:SerializedName("date")
    val date: String? = null,

    @field:SerializedName("detected_cocoas")
    val detectedCocoas: List<DetectedCocoaDto?>? = null,

    @field:SerializedName("session_name")
    val sessionName: String? = null,

    @field:SerializedName("solution_en")
    val solutionEn: String? = null,

    @field:SerializedName("user_id")
    val userId: Int? = null,

    @field:SerializedName("session_image")
    val sessionImage: String? = null,

    @field:SerializedName("session_id")
    val sessionId: Int? = null,

    @field:SerializedName("prevention_id")
    val preventionId: String? = null,

    @field:SerializedName("prevention_en")
    val preventionEn: String? = null,

    @field:SerializedName("solution_id")
    val solutionId: String? = null,

    @field:SerializedName("total_predicted_price")
    val totalPredictedPrice: Float? = null
)


