package com.glazer.flying.spaghetti.monster.gospel.bible.workmanager

import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.glazer.flying.spaghetti.monster.gospel.bible.domain.repository.NotificationRepository
import com.glazer.flying.spaghetti.monster.gospel.bible.extensions.calculateDelay
import com.glazer.flying.spaghetti.monster.gospel.bible.utils.Constants.DAILY_WORK_NAME
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    private val workManager: WorkManager,
) : NotificationRepository {
    
    override suspend fun scheduleNotification(hour: Int, minute: Int) {
        val delay = calculateDelay(hour, minute)
        val request = PeriodicWorkRequestBuilder<NotificationWorker>(1, TimeUnit.DAYS)
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .build()

        workManager.enqueueUniquePeriodicWork(
            DAILY_WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )
    }

    override fun cancelNotificationWork() {
       workManager.cancelUniqueWork(DAILY_WORK_NAME)
    }
}