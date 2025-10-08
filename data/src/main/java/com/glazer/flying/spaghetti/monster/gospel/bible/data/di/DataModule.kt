package com.glazer.flying.spaghetti.monster.gospel.bible.data.di

import android.content.Context
import com.glazer.flying.spaghetti.monster.gospel.bible.data.sharedpreferences.PrefsManagerImpl
import com.glazer.flying.spaghetti.monster.gospel.bible.domain.sharedpreferences.PrefsManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun providePrefsManager(@ApplicationContext context: Context): PrefsManager {
        return PrefsManagerImpl(context)
    }
}