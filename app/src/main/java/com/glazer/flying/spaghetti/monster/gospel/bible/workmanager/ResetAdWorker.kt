package com.glazer.flying.spaghetti.monster.gospel.bible.workmanager

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.glazer.flying.spaghetti.monster.gospel.bible.domain.repository.PreferencesRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class ResetAdWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val repository: PreferencesRepository
) : CoroutineWorker(appContext, params) {
    
    override suspend fun doWork(): Result {
        return try {
            repository.setAdviceAmount(0)
            repository.setIsDialogShowed(false)
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}