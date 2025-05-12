package com.neotelemetrixgdscunand.kakaoxpert.data.remote.dto

import com.google.gson.annotations.SerializedName


data class Response<T>(
    @field:SerializedName("success")
    val success: Boolean = false,
    @field:SerializedName("data")
    val data: T? = null,
    @field:SerializedName("message")
    val message: String? = null
)