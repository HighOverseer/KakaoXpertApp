package com.neotelemetrixgdscunand.kakaoxpert.di

import com.neotelemetrixgdscunand.kakaoxpert.domain.data.AuthPreference
import com.neotelemetrixgdscunand.kakaoxpert.domain.data.CocoaAnalysisRepository
import com.neotelemetrixgdscunand.kakaoxpert.domain.data.DataPreference
import com.neotelemetrixgdscunand.kakaoxpert.domain.interactor.AnalysisCocoaInteractor
import com.neotelemetrixgdscunand.kakaoxpert.domain.interactor.GetCocoaAnalysisSessionInteractor
import com.neotelemetrixgdscunand.kakaoxpert.domain.interactor.LogoutInteractor
import com.neotelemetrixgdscunand.kakaoxpert.domain.presentation.CocoaDamageLevelPredictionHelper
import com.neotelemetrixgdscunand.kakaoxpert.domain.presentation.CocoaDiseaseDetectorHelper
import com.neotelemetrixgdscunand.kakaoxpert.domain.presentation.CocoaPriceCalculationHelper
import com.neotelemetrixgdscunand.kakaoxpert.domain.usecase.AnalysisCocoaUseCase
import com.neotelemetrixgdscunand.kakaoxpert.domain.usecase.GetCocoaAnalysisSessionUseCase
import com.neotelemetrixgdscunand.kakaoxpert.domain.usecase.LogoutUseCase
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
        cocoaDiseaseDetectorHelper: CocoaDiseaseDetectorHelper,
        cocoaPriceCalculationHelper: CocoaPriceCalculationHelper,
        cocoaDamageLevelPredictionHelper: CocoaDamageLevelPredictionHelper
    ): AnalysisCocoaUseCase {
        return AnalysisCocoaInteractor(
            cocoaDiseaseDetectorHelper,
            cocoaDamageLevelPredictionHelper,
            cocoaPriceCalculationHelper,
            cocoaAnalysisRepository
        )
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
    ): LogoutUseCase {
        return LogoutInteractor(
            authPreference,
            dataPreference,
            cocoaAnalysisRepository
        )
    }
}