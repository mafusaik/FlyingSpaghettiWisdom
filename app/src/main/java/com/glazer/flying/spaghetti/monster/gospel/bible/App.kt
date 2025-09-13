package com.glazer.flying.spaghetti.monster.gospel.bible

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.glazer.flying.spaghetti.monster.gospel.bible.utils.Constants
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class BibleFSMApp : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override val workManagerConfiguration: Configuration
        get() =  Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    private fun createNotificationChannel() {
        val channelReminders = NotificationChannel(
            Constants.CHANNEL_ID,
            Constants.ADVICES,
            NotificationManager.IMPORTANCE_HIGH,
        ).apply {
            description = Constants.ADVICES
            enableVibration(true)
        }

        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channelReminders)
    }
}