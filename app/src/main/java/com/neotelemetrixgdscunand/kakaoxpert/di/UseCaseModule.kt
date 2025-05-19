package com.neotelemetrixgdscunand.kakaoxpert.di

import com.neotelemetrixgdscunand.kakaoxpert.domain.data.AuthPreference
import com.neotelemetrixgdscunand.kakaoxpert.domain.data.CocoaAnalysisRepository
import com.neotelemetrixgdscunand.kakaoxpert.domain.data.DataPreference
import com.neotelemetrixgdscunand.kakaoxpert.domain.interactor.AnalysisCocoaInteractor
import com.neotelemetrixgdscunand.kakaoxpert.domain.interactor.GetCocoaAnalysisSessionInteractor
import com.neotelemetrixgdscunand.kakaoxpert.domain.interactor.LogoutInteractor
import com.neotelemetrixgdscunand.kakaoxpert.domain.interactor.SyncCocoaAnalysisDataInteractor
import com.neotelemetrixgdscunand.kakaoxpert.domain.presentation.CocoaImageDetectorHelper
import com.neotelemetrixgdscunand.kakaoxpert.domain.usecase.AnalysisCocoaUseCase
import com.neotelemetrixgdscunand.kakaoxpert.domain.usecase.GetCocoaAnalysisSessionUseCase
import com.neotelemetrixgdscunand.kakaoxpert.domain.usecase.LogoutUseCase
import com.neotelemetrixgdscunand.kakaoxpert.domain.usecase.SyncCocoaAnalysisDataUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
class UseCaseModule {

    @Provides
    @ViewModelScoped
    fun provideAnalysisCocoaUseCase(
        cocoaAnalysisRepository: CocoaAnalysisRepository,
        cocoaImageDetectorHelper: CocoaImageDetectorHelper
    ): AnalysisCocoaUseCase {
        return AnalysisCocoaInteractor(cocoaImageDetectorHelper, cocoaAnalysisRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideGetCocoaAnalysisSessionUseCase(
        cocoaAnalysisRepository: CocoaAnalysisRepository
    ): GetCocoaAnalysisSessionUseCase {
        return GetCocoaAnalysisSessionInteractor(cocoaAnalysisRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideLogoutUseCase(
        cocoaAnalysisRepository: CocoaAnalysisRepository,
        dataPreference: DataPreference,
        authPreference: AuthPreference
    ):LogoutUseCase{
        return LogoutInteractor(
            authPreference,
            dataPreference,
            cocoaAnalysisRepository
        )
    }
}