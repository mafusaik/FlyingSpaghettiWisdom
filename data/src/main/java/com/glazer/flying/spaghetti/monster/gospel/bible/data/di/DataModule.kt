package com.glazer.flying.spaghetti.monster.gospel.bible.data.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.glazer.flying.spaghetti.monster.gospel.bible.data.database.GospelDao
import com.glazer.flying.spaghetti.monster.gospel.bible.data.database.GospelDatabase
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
    fun provideDatabase(app: Application): GospelDatabase {
        return Room.databaseBuilder(
            app,
            GospelDatabase::class.java,
            "gospel.db"
        ).build()
    }

    @Provides
    fun provideDao(db: GospelDatabase): GospelDao = db.gospelDao()

    @Provides
    @Singleton
    fun providePrefsManager(@ApplicationContext context: Context): PrefsManager {
        return PrefsManagerImpl(context)
    }
}