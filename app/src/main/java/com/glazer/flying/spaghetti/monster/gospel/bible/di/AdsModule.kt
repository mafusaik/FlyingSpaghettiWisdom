package com.glazer.flying.spaghetti.monster.gospel.bible.di

import android.content.Context
import android.telephony.TelephonyManager
import com.glazer.flying.spaghetti.monster.gospel.bible.ads.AdManager
import com.glazer.flying.spaghetti.monster.gospel.bible.ads.GoogleAdManager
import com.glazer.flying.spaghetti.monster.gospel.bible.ads.YandexAdManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.util.Locale
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AdsModule {

    @Provides
    @Singleton
    fun provideAdManager(@ApplicationContext context: Context): AdManager {
        val country = getUserCountry(context)

        return if (country.equals("RU", ignoreCase = true) ||
            country.equals("BY", ignoreCase = true) ||
            country.equals("KZ", ignoreCase = true)
            ) {
            YandexAdManager(context)
        } else {
            GoogleAdManager(context)
        }
    }

    private fun getUserCountry(context: Context): String? {
        val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as? TelephonyManager
        val fromNetwork = tm?.networkCountryIso?.takeIf { it.isNotBlank() }?.lowercase(Locale.ROOT)
        if (!fromNetwork.isNullOrEmpty()) return fromNetwork.uppercase(Locale.ROOT)

        val fromSim = tm?.simCountryIso?.takeIf { it.isNotBlank() }?.lowercase(Locale.ROOT)
        if (!fromSim.isNullOrEmpty()) return fromSim.uppercase(Locale.ROOT)

        val localeCountry = try {
            val locale = context.resources.configuration.locales.get(0)
            locale?.country
        } catch (e: Exception) {
            null
        }
        if (!localeCountry.isNullOrBlank()) return localeCountry.uppercase(Locale.ROOT)

        val defaultCountry = Locale.getDefault().country
        if (!defaultCountry.isNullOrBlank()) return defaultCountry.uppercase(Locale.ROOT)
        return null
    }
}