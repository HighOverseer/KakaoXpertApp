package com.neotelemetrixgdscunand.kakaoxpert.data.remote

import com.neotelemetrixgdscunand.kakaoxpert.data.remote.dto.CocoaSellPriceInfoDto
import com.neotelemetrixgdscunand.kakaoxpert.data.remote.dto.Response
import retrofit2.http.GET

interface CocoaPriceInfoService {

    @GET("price-info")
    suspend fun getAllLatest(): Response<CocoaSellPriceInfoDto>
}