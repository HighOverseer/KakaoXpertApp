package com.neotelemetrixgdscunand.kakaoxpert.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.neotelemetrixgdscunand.kakaoxpert.data.local.database.CocoaAnalysisDatabase
import com.neotelemetrixgdscunand.kakaoxpert.domain.data.AuthPreference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(AuthPreference.NAME)

@Module
@InstallIn(SingletonComponent::class)
class LocalStorageModule {

    @Provides
    @Singleton
    fun provideDataStore(
        @ApplicationContext context: Context
    ): DataStore<Preferences> {
        return context.dataStore
    }

    @Provides
    @Singleton
    fun provideCocoaAnalysisDatabase(
        @ApplicationContext context: Context
    ):CocoaAnalysisDatabase{
        return Room.databaseBuilder(
            context.applicationContext,
            CocoaAnalysisDatabase::class.java,
            CocoaAnalysisDatabase.DATABASE_NAME
        ).build()
    }
}