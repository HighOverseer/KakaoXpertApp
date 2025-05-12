package com.neotelemetrixgdscunand.kakaoxpert.di

import com.google.i18n.phonenumbers.PhoneNumberUtil
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@InstallIn(ViewModelComponent::class)
@Module
class AuthModule {

    @Provides
    fun providePhoneNumberUtils(): PhoneNumberUtil {
        return PhoneNumberUtil.getInstance()
    }


}