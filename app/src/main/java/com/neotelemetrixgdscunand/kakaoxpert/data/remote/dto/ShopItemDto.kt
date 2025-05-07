package com.neotelemetrixgdscunand.kamekapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ShopItemDto(

    @field:SerializedName("image_url")
    val imageUrl: String? = null,

    @field:SerializedName("price")
    val price: Long? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("link")
    val link: String? = null,

    @field:SerializedName("shop_item_id")
    val id: Int? = null
)
