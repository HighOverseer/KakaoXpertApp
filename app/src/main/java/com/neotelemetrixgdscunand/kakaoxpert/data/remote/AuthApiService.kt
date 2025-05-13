package com.neotelemetrixgdscunand.kakaoxpert.data.remote

import com.neotelemetrixgdscunand.kakaoxpert.data.remote.dto.AnalysisSessionDto
import com.neotelemetrixgdscunand.kakaoxpert.data.remote.dto.AnalysisSessionPreviewDto
import com.neotelemetrixgdscunand.kakaoxpert.data.remote.dto.LoginDto
import com.neotelemetrixgdscunand.kakaoxpert.data.remote.dto.NewsDetailsDto
import com.neotelemetrixgdscunand.kakaoxpert.data.remote.dto.NewsItemDto
import com.neotelemetrixgdscunand.kakaoxpert.data.remote.dto.RegisterDto
import com.neotelemetrixgdscunand.kakaoxpert.data.remote.dto.Response
import com.neotelemetrixgdscunand.kakaoxpert.data.remote.dto.ShopItemDto
import com.neotelemetrixgdscunand.kakaoxpert.data.remote.dto.WeatherForecastItemDto
import com.neotelemetrixgdscunand.kakaoxpert.data.remote.dto.WeatherForecastOverviewDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface AuthApiService {

    @POST("register")
    @FormUrlEncoded
    suspend fun register(
        @Field("no_hp") handphoneNumberOrEmail: String,
        @Field("password") password: String,
        @Field("confirm_password") passwordConfirmation: String,
        @Field("name") name: String
    ): Response<RegisterDto>

    @POST("login")
    @FormUrlEncoded
    suspend fun login(
        @Field("no_hp") handphoneNumberOrEmail: String,
        @Field("password") password: String
    ): Response<LoginDto>
}