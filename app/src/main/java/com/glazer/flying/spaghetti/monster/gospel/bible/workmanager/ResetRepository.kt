package com.glazer.flying.spaghetti.monster.gospel.bible.workmanager

import android.util.Log
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.glazer.flying.spaghetti.monster.gospel.bible.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.time.Duration
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ResetRepository @Inject constructor(
    private val workManager: WorkManager
) {

    suspend fun isWorkDisable(): Flow<Boolean>{
       val listWorks = withContext(Dispatchers.IO) {
           workManager.getWorkInfosForUniqueWorkFlow(Constants.RESET_WORK_NAME).map { workInfos ->
               workInfos.any { workInfo ->
                   workInfo.state == WorkInfo.State.ENQUEUED ||
                           workInfo.state == WorkInfo.State.RUNNING ||
                           workInfo.state == WorkInfo.State.BLOCKED
               }
           }
       }
       return listWorks
    }

    fun scheduleDailyResetWork() {
        Log.i("RESET_WORK", "scheduleDailyResetWork")
        val delay = calculateDelay(3, 0)

        val workRequest = PeriodicWorkRequestBuilder<ResetAdWorker>(1, TimeUnit.DAYS)
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .build()

        workManager.enqueueUniquePeriodicWork(
            Constants.RESET_WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }

    fun cancelWork() {
        workManager.cancelUniqueWork(Constants.RESET_WORK_NAME)
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