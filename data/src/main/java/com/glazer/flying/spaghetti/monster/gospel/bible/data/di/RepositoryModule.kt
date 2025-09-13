package com.glazer.flying.spaghetti.monster.gospel.bible.data.di

import android.content.Context
import com.glazer.flying.spaghetti.monster.gospel.bible.data.repositoryimpl.AdviceRepositoryImpl
import com.glazer.flying.spaghetti.monster.gospel.bible.data.repositoryimpl.PdfRepositoryImpl
import com.glazer.flying.spaghetti.monster.gospel.bible.data.repositoryimpl.PreferencesRepositoryImpl
import com.glazer.flying.spaghetti.monster.gospel.bible.data.repositoryimpl.SettingsRepositoryImpl
import com.glazer.flying.spaghetti.monster.gospel.bible.data.repositoryimpl.SystemClipboardImpl
import com.glazer.flying.spaghetti.monster.gospel.bible.data.utils.PdfRendererManager
import com.glazer.flying.spaghetti.monster.gospel.bible.domain.repository.AdviceRepository
import com.glazer.flying.spaghetti.monster.gospel.bible.domain.repository.PdfRepository
import com.glazer.flying.spaghetti.monster.gospel.bible.domain.repository.PreferencesRepository
import com.glazer.flying.spaghetti.monster.gospel.bible.domain.repository.SettingsRepository
import com.glazer.flying.spaghetti.monster.gospel.bible.domain.repository.SystemClipboard
import com.glazer.flying.spaghetti.monster.gospel.bible.domain.sharedpreferences.PrefsManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun providePdfRendererManager(@ApplicationContext context: Context): PdfRendererManager {
        return PdfRendererManager(context)
    }

    @Provides
    @Singleton
    fun providePdfRepository(rendererManager: PdfRendererManager): PdfRepository {
        return PdfRepositoryImpl(rendererManager)
    }

    @Provides
    @Singleton
    fun provideSystemClipboard(@ApplicationContext context: Context): SystemClipboard {
        return SystemClipboardImpl(context)
    }

    @Provides
    @Singleton
    fun provideAdviceRepository(@ApplicationContext context: Context, prefManager: PrefsManager): AdviceRepository {
        return AdviceRepositoryImpl(context, prefManager)
    }

    @Provides
    @Singleton
    fun providePreferencesRepository(prefManager: PrefsManager): PreferencesRepository {
        return PreferencesRepositoryImpl(prefManager)
    }

    @Provides
    @Singleton
    fun provideSettingsRepository(): SettingsRepository {
        return SettingsRepositoryImpl()
    }
}