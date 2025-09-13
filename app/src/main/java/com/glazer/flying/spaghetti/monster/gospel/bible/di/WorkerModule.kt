package com.glazer.flying.spaghetti.monster.gospel.bible.di

import android.content.Context
import androidx.work.WorkManager
import com.glazer.flying.spaghetti.monster.gospel.bible.domain.repository.NotificationRepository
import com.glazer.flying.spaghetti.monster.gospel.bible.workmanager.NotificationRepositoryImpl
import com.glazer.flying.spaghetti.monster.gospel.bible.workmanager.ResetRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WorkerModule {
    @Provides
    @Singleton
    fun provideWorkManager(@ApplicationContext context: Context): WorkManager {
        return WorkManager.getInstance(context)
    }

    @Provides
    @Singleton
    fun provideNotificationRepository(workManager: WorkManager): NotificationRepository {
        return NotificationRepositoryImpl(workManager)
    }

    @Provides
    @Singleton
    fun provideResetRepository(workManager: WorkManager): ResetRepository {
        return ResetRepository(workManager)
    }
}