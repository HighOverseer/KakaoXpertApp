package com.neotelemetrixgdscunand.kakaoxpert.data.remote

import com.neotelemetrixgdscunand.kakaoxpert.data.remote.dto.LoginDto
import com.neotelemetrixgdscunand.kakaoxpert.data.remote.dto.RegisterDto
import com.neotelemetrixgdscunand.kakaoxpert.data.remote.dto.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

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