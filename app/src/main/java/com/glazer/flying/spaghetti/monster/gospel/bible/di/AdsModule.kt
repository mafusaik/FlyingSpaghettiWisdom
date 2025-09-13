package com.glazer.flying.spaghetti.monster.gospel.bible.di

import android.content.Context
import com.glazer.flying.spaghetti.monster.gospel.bible.ads.RewardedAdRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AdsModule {

    @Provides
    @Singleton
    fun provideRewardedAdRepository(
        @ApplicationContext context: Context
    ): RewardedAdRepository = RewardedAdRepository(context)

}