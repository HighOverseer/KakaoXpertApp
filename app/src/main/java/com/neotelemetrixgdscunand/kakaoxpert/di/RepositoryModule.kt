package com.neotelemetrixgdscunand.kakaoxpert.di

import com.neotelemetrixgdscunand.kakaoxpert.data.RepositoryImpl
import com.neotelemetrixgdscunand.kakaoxpert.domain.data.Repository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun provideRepository(repository: RepositoryImpl): Repository

}