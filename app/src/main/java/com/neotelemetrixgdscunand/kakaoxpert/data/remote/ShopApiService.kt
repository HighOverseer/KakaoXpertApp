package com.neotelemetrixgdscunand.kakaoxpert.data.remote

import com.neotelemetrixgdscunand.kakaoxpert.data.remote.dto.AnalysisSessionDto
import com.neotelemetrixgdscunand.kakaoxpert.data.remote.dto.AnalysisSessionPreviewDto
import com.neotelemetrixgdscunand.kakaoxpert.data.remote.dto.Response
import com.neotelemetrixgdscunand.kakaoxpert.data.remote.dto.ShopItemDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ShopApiService {
    @GET("shop-item")
    suspend fun getShopItems(
        @Query("q") query: String = ""
    ): Response<List<ShopItemDto>>
}