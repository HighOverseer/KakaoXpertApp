package com.neotelemetrixgdscunand.kakaoxpert.di

import android.content.Context
import android.location.Geocoder
import com.neotelemetrixgdscunand.kakaoxpert.data.DataMapper
import com.neotelemetrixgdscunand.kakaoxpert.data.WeatherDtoMapper
import com.neotelemetrixgdscunand.kakaoxpert.data.local.database.EntityMapper
import com.neotelemetrixgdscunand.kakaoxpert.domain.data.CocoaAnalysisRepository
import com.neotelemetrixgdscunand.kakaoxpert.domain.data.DataPreference
import com.neotelemetrixgdscunand.kakaoxpert.domain.interactor.SyncCocoaAnalysisDataInteractor
import com.neotelemetrixgdscunand.kakaoxpert.domain.usecase.SyncCocoaAnalysisDataUseCase
import com.neotelemetrixgdscunand.kakaoxpert.presentation.mapper.DuiMapper
import com.neotelemetrixgdscunand.kakaoxpert.presentation.mapper.WeatherDuiMapper
import com.neotelemetrixgdscunand.kakaoxpert.presentation.worker.CommonWorkerFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    fun provideDuiMapper(): DuiMapper = DuiMapper

    @Provides
    fun provideDataMapper(): DataMapper = DataMapper

    @Provides
    fun provideEntityMapper(): EntityMapper = EntityMapper

    @Provides
    fun provideWeatherDuiMapper(): WeatherDuiMapper = WeatherDuiMapper

    @Provides
    fun provideWeatherDtoMapper(): WeatherDtoMapper = WeatherDtoMapper

    @Provides
    fun provideGeoCoder(
        @ApplicationContext context: Context
    ): Geocoder = Geocoder(context)

    @Provides
    @Singleton
    fun provideSyncCocoaAnalysisDataInteractor(
        dataPreference: DataPreference,
        cocoaAnalysisRepository: CocoaAnalysisRepository
    ): SyncCocoaAnalysisDataInteractor {
        return SyncCocoaAnalysisDataInteractor(
            cocoaAnalysisRepository,
            dataPreference
        )
    }

    @Provides
    @Singleton
    fun provideWorkerFactory(
        syncCocoaAnalysisDataUseCase: SyncCocoaAnalysisDataUseCase,
    ): CommonWorkerFactory {
        return CommonWorkerFactory(syncCocoaAnalysisDataUseCase)
    }
}