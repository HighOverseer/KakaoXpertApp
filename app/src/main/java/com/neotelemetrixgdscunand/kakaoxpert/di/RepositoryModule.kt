package com.neotelemetrixgdscunand.kakaoxpert.di

import com.neotelemetrixgdscunand.kakaoxpert.data.AuthRepositoryImpl
import com.neotelemetrixgdscunand.kakaoxpert.data.CocoaAnalysisRepositoryImpl
import com.neotelemetrixgdscunand.kakaoxpert.data.IoTDeviceRepositoryImpl
import com.neotelemetrixgdscunand.kakaoxpert.data.NewsRepositoryImpl
import com.neotelemetrixgdscunand.kakaoxpert.data.ShopRepositoryImpl
import com.neotelemetrixgdscunand.kakaoxpert.data.WeatherRepositoryImpl
import com.neotelemetrixgdscunand.kakaoxpert.data.local.preference.AuthPreferenceImpl
import com.neotelemetrixgdscunand.kakaoxpert.data.local.preference.DataPreferenceImpl
import com.neotelemetrixgdscunand.kakaoxpert.domain.data.AuthPreference
import com.neotelemetrixgdscunand.kakaoxpert.domain.data.AuthRepository
import com.neotelemetrixgdscunand.kakaoxpert.domain.data.CocoaAnalysisRepository
import com.neotelemetrixgdscunand.kakaoxpert.domain.data.DataPreference
import com.neotelemetrixgdscunand.kakaoxpert.domain.data.IoTDeviceRepository
import com.neotelemetrixgdscunand.kakaoxpert.domain.data.NewsRepository
import com.neotelemetrixgdscunand.kakaoxpert.domain.data.ShopRepository
import com.neotelemetrixgdscunand.kakaoxpert.domain.data.WeatherRepository
import com.neotelemetrixgdscunand.kakaoxpert.domain.interactor.SyncCocoaAnalysisDataInteractor
import com.neotelemetrixgdscunand.kakaoxpert.domain.usecase.SyncCocoaAnalysisDataUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindRepository(repository: CocoaAnalysisRepositoryImpl): CocoaAnalysisRepository

    @Binds
    @Singleton
    abstract fun bindAuthRepository(authRepository: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindWeatherRepository(weatherRepository: WeatherRepositoryImpl): WeatherRepository

    @Binds
    @Singleton
    abstract fun bindNewsRepository(newsRepositoryImpl: NewsRepositoryImpl): NewsRepository

    @Binds
    @Singleton
    abstract fun bindShopRepository(shopRepositoryImpl: ShopRepositoryImpl): ShopRepository

    @Binds
    @Singleton
    abstract fun bindIoTDeviceRepository(iotDeviceRepositoryImpl: IoTDeviceRepositoryImpl): IoTDeviceRepository

    @Binds
    abstract fun bindAuthPreference(authPreferenceImpl: AuthPreferenceImpl): AuthPreference

    @Binds
    abstract fun bindDataPreference(dataPreferenceImpl: DataPreferenceImpl): DataPreference

    @Binds
    abstract fun bindSyncCocoaAnalysisDataUseCase(syncCocoaAnalysisDataInteractor: SyncCocoaAnalysisDataInteractor): SyncCocoaAnalysisDataUseCase

}