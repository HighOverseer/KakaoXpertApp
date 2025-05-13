package com.neotelemetrixgdscunand.kakaoxpert.data.remote

import com.neotelemetrixgdscunand.kakaoxpert.data.remote.dto.NewsDetailsDto
import com.neotelemetrixgdscunand.kakaoxpert.data.remote.dto.NewsItemDto
import com.neotelemetrixgdscunand.kakaoxpert.data.remote.dto.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface NewsApiService {
    @GET("news")
    suspend fun getNewsItems(
        @Query("q") query: String = "",
        @Query("type") newsTypeId: Int,
    ): Response<List<NewsItemDto>>

    @GET("news/{id}")
    suspend fun getNewsById(
        @Path("id") newsId: Int,
        @Query("type") newsTypeId: Int,
    ): Response<NewsDetailsDto>
}