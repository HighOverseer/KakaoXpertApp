package com.neotelemetrixgdscunand.kakaoxpert.data.remote.dto

import com.google.gson.annotations.SerializedName

data class LoginDto(

    @field:SerializedName("user_id")
    val userId: Int? = null,

    @field:SerializedName("user_name")
    val userName: String? = null,

    @field:SerializedName("token")
    val token: String? = null
)
