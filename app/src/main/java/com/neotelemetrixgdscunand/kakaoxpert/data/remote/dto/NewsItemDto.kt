package com.neotelemetrixgdscunand.kakaoxpert.data.remote.dto

import com.google.gson.annotations.SerializedName

data class NewsItemDto(

    @field:SerializedName("date")
    val date: String? = null,

    @field:SerializedName("image_url")
    val imageUrl: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("headline")
    val headline: String? = null
)
