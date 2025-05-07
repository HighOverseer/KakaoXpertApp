package com.neotelemetrixgdscunand.kakaoxpert.data.remote

import com.neotelemetrixgdscunand.kamekapp.domain.data.AuthPreference
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val authPreference: AuthPreference
):Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val origin = chain.request()
        val token:String
        return runBlocking {
            token = authPreference.getToken().first()
            if (token.isNotEmpty()){
                val authorized = origin.newBuilder()
                    .addHeader("Authorization", "Bearer $token")
                    .build()

                chain.proceed(authorized)

            } else chain.proceed(origin)
        }
    }
}