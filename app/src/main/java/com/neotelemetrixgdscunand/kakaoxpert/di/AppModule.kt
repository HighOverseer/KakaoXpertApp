package com.neotelemetrixgdscunand.kakaoxpert.di

import android.content.Context
import android.location.Geocoder
import com.neotelemetrixgdscunand.kakaoxpert.data.DataMapper
import com.neotelemetrixgdscunand.kakaoxpert.data.WeatherDtoMapper
import com.neotelemetrixgdscunand.kakaoxpert.presentation.mapper.DuiMapper
import com.neotelemetrixgdscunand.kakaoxpert.presentation.mapper.WeatherDuiMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    fun provideDuiMapper(): DuiMapper = DuiMapper

    @Provides
    fun provideDataMapper(): DataMapper = DataMapper

    @Provides
    fun provideWeatherDuiMapper(): WeatherDuiMapper = WeatherDuiMapper

    @Provides
    fun provideWeatherDtoMapper(): WeatherDtoMapper = WeatherDtoMapper

    @Provides
    fun provideGeoCoder(
        @ApplicationContext context: Context
    ): Geocoder = Geocoder(context)
}