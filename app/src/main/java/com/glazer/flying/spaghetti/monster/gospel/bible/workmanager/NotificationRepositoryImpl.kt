package com.glazer.flying.spaghetti.monster.gospel.bible.workmanager

import android.util.Log
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.glazer.flying.spaghetti.monster.gospel.bible.domain.repository.NotificationRepository
import java.time.Duration
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    private val workManager: WorkManager,
) : NotificationRepository {
    
    override suspend fun scheduleNotification(hour: Int, minute: Int) {
        val delay = calculateDelay(hour, minute)
        Log.i("WORK_MANAGER", "delay $delay $hour $minute")
        val request = PeriodicWorkRequestBuilder<NotificationWorker>(1, TimeUnit.DAYS)
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .build()

        workManager.enqueueUniquePeriodicWork(
            "daily_notification",
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )
    }

    override fun cancelAllNotifications() {
       Log.i("WORK_MANAGER", "cancelAllWork")
       workManager.cancelUniqueWork("daily_notification")
    }

    private fun calculateDelay(hour: Int, minute: Int): Long {
        val now = LocalDateTime.now()
        var scheduledTime = now.withHour(hour).withMinute(minute).withSecond(0).withNano(0)
        if (scheduledTime.isBefore(now)) {
            scheduledTime = scheduledTime.plusDays(1)
        }
        return Duration.between(now, scheduledTime).toMillis()
    }
}