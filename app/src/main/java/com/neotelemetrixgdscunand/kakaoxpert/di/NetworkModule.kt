package com.neotelemetrixgdscunand.kakaoxpert.di

import com.neotelemetrixgdscunand.kakaoxpert.BuildConfig
import com.neotelemetrixgdscunand.kakaoxpert.data.remote.AuthApiService
import com.neotelemetrixgdscunand.kakaoxpert.data.remote.AuthInterceptor
import com.neotelemetrixgdscunand.kakaoxpert.data.remote.CocoaAnalysisApiService
import com.neotelemetrixgdscunand.kakaoxpert.data.remote.CocoaPriceInfoService
import com.neotelemetrixgdscunand.kakaoxpert.data.remote.IoTDeviceService
import com.neotelemetrixgdscunand.kakaoxpert.data.remote.NewsApiService
import com.neotelemetrixgdscunand.kakaoxpert.data.remote.ShopApiService
import com.neotelemetrixgdscunand.kakaoxpert.data.remote.WeatherApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor
    ): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        }


        return OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideGsonConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    @Provides
    fun provideCocoaAnalysisApiService(
        client: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): CocoaAnalysisApiService {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(gsonConverterFactory)
            .client(client)
            .build()
            .create(CocoaAnalysisApiService::class.java)
    }

    @Provides
    fun provideShopApiService(
        client: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): ShopApiService {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(gsonConverterFactory)
            .client(client)
            .build()
            .create(ShopApiService::class.java)
    }

    @Provides
    fun provideNewsApiService(
        client: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): NewsApiService {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(gsonConverterFactory)
            .client(client)
            .build()
            .create(NewsApiService::class.java)
    }

    @Provides
    fun provideWeatherApiService(
        client: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): WeatherApiService {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(gsonConverterFactory)
            .client(client)
            .build()
            .create(WeatherApiService::class.java)
    }

    @Provides
    fun provideAuthApiService(
        client: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): AuthApiService {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(gsonConverterFactory)
            .client(client)
            .build()
            .create(AuthApiService::class.java)
    }

    @Provides
    fun provideIoTDeviceService(
        client: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): IoTDeviceService {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(gsonConverterFactory)
            .client(client)
            .build()
            .create(IoTDeviceService::class.java)
    }

    @Provides
    fun provideCocoaPriceInfoService(
        client: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): CocoaPriceInfoService {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(gsonConverterFactory)
            .client(client)
            .build()
            .create(CocoaPriceInfoService::class.java)
    }
}