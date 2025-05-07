package com.neotelemetrixgdscunand.kamekapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class NewsDetailsDto(

    @field:SerializedName("date")
    val date: String? = null,

    @field:SerializedName("image_url")
    val imageUrl: String? = null,

    @field:SerializedName("description")
    val description: String? = null,

    @field:SerializedName("headline")
    val headline: String? = null,
)
