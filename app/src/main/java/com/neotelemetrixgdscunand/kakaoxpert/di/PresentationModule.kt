package com.neotelemetrixgdscunand.kamekapp.di

import com.neotelemetrixgdscunand.kakaoxpert.domain.presentation.CaptureImageFileHandler
import com.neotelemetrixgdscunand.kakaoxpert.domain.presentation.ImageDetectorHelper
import com.neotelemetrixgdscunand.kakaoxpert.domain.presentation.ModelLabelExtractor
import com.neotelemetrixgdscunand.kamekapp.data.utils.CapturedImageFileHandlerImpl
import com.neotelemetrixgdscunand.kamekapp.data.utils.ImageDetectorHelperImpl
import com.neotelemetrixgdscunand.kamekapp.data.utils.LocationManagerImpl
import com.neotelemetrixgdscunand.kamekapp.data.utils.ModelLabelExtractorImpl
import com.neotelemetrixgdscunand.kamekapp.domain.common.PasswordValidator
import com.neotelemetrixgdscunand.kamekapp.domain.common.UsernameValidator
import com.neotelemetrixgdscunand.kamekapp.domain.data.LocationManager
import com.neotelemetrixgdscunand.kamekapp.presentation.utils.PasswordValidatorImpl
import com.neotelemetrixgdscunand.kamekapp.presentation.utils.UsernameValidatorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class PresentationModule {

    @Binds
    abstract fun bindImageDetectorHelper(imageDetectorHelperImpl: ImageDetectorHelperImpl): ImageDetectorHelper

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