package com.neotelemetrixgdscunand.kakaoxpert.di

import com.neotelemetrixgdscunand.kakaoxpert.data.utils.CapturedImageFileHandlerImpl
import com.neotelemetrixgdscunand.kakaoxpert.data.utils.CocoaDamageLevelPredictionHelperImpl
import com.neotelemetrixgdscunand.kakaoxpert.data.utils.CocoaDiseaseDetectorHelperImpl
import com.neotelemetrixgdscunand.kakaoxpert.data.utils.LocationManagerImpl
import com.neotelemetrixgdscunand.kakaoxpert.data.utils.ModelLabelExtractorImpl
import com.neotelemetrixgdscunand.kakaoxpert.domain.common.PasswordValidator
import com.neotelemetrixgdscunand.kakaoxpert.domain.common.UsernameValidator
import com.neotelemetrixgdscunand.kakaoxpert.domain.data.LocationManager
import com.neotelemetrixgdscunand.kakaoxpert.domain.presentation.CaptureImageFileHandler
import com.neotelemetrixgdscunand.kakaoxpert.domain.presentation.CocoaDamageLevelPredictionHelper
import com.neotelemetrixgdscunand.kakaoxpert.domain.presentation.CocoaDiseaseDetectorHelper
import com.neotelemetrixgdscunand.kakaoxpert.domain.presentation.ModelLabelExtractor
import com.neotelemetrixgdscunand.kakaoxpert.presentation.utils.PasswordValidatorImpl
import com.neotelemetrixgdscunand.kakaoxpert.presentation.utils.UsernameValidatorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class PresentationModule {

    @Binds
    abstract fun bindImageDetectorHelper(imageDetectorHelperImpl: CocoaDiseaseDetectorHelperImpl): CocoaDiseaseDetectorHelper

    @Binds
    abstract fun bindCocoaPricePredictionHelper(cocoaDamageLevelPredictionHelperImpl: CocoaDamageLevelPredictionHelperImpl): CocoaDamageLevelPredictionHelper

    @Binds
    @ViewModelScoped
    abstract fun bindModelLabelExtractor(modelLabelExtractorImpl: ModelLabelExtractorImpl): ModelLabelExtractor

    @Binds
    abstract fun bindCapturedImageFileHandler(capturedImageFileHandlerImpl: CapturedImageFileHandlerImpl): CaptureImageFileHandler

    @Binds
    abstract fun bindUsernameValidator(usernameValidatorImpl: UsernameValidatorImpl): UsernameValidator

    @Binds
    abstract fun bindPasswordValidator(passwordValidatorImpl: PasswordValidatorImpl): PasswordValidator

    @Binds
    abstract fun bindLocationManager(locationManagerImpl: LocationManagerImpl): LocationManager

}