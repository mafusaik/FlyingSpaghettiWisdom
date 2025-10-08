package com.glazer.flying.spaghetti.monster.gospel.bible.workmanager

import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.glazer.flying.spaghetti.monster.gospel.bible.extensions.calculateDelay
import com.glazer.flying.spaghetti.monster.gospel.bible.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
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
}