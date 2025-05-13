package com.neotelemetrixgdscunand.kakaoxpert.data.remote

import com.neotelemetrixgdscunand.kakaoxpert.data.remote.dto.Response
import com.neotelemetrixgdscunand.kakaoxpert.data.remote.dto.ShopItemDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ShopApiService {
    @GET("shop-item")
    suspend fun getShopItems(
        @Query("q") query: String = ""
    ): Response<List<ShopItemDto>>
}