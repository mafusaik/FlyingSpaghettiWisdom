package com.glazer.flying.spaghetti.monster.gospel.bible.workmanager

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.glazer.flying.spaghetti.monster.gospel.bible.MainActivity
import com.glazer.flying.spaghetti.monster.gospel.bible.R
import com.glazer.flying.spaghetti.monster.gospel.bible.domain.repository.AdviceRepository
import com.glazer.flying.spaghetti.monster.gospel.bible.navigation.BottomScreens
import com.glazer.flying.spaghetti.monster.gospel.bible.utils.Constants
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class NotificationWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val repository: AdviceRepository
) : CoroutineWorker(appContext, params) {

    private val context = appContext
    
    override suspend fun doWork(): Result {
        return try {
            Log.i("WORK_MANAGER", "doWork")
            val message = repository.nextAdvice()
            showNotification(message)
            Result.success(workDataOf(Constants.KEY_MESSAGE to message))
        } catch (e: Exception) {
            Result.failure()
        }
    }
    
    @SuppressLint("MissingPermission")
    private fun showNotification(message: String) {
        Log.i("WORK_MANAGER", "showNotification $message")
        val notificationManager = NotificationManagerCompat.from(context)

        val pendingIntent = createPendingIntent(BottomScreens.Advice.route)

        val notification = NotificationCompat.Builder(context, Constants.CHANNEL_ID)
            .setContentText(message)
            .setSmallIcon(R.drawable.logo_fsm)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        val notificationId = System.currentTimeMillis().toInt()
        notificationManager.notify(notificationId, notification)
    }

    private fun createPendingIntent(destination: String): PendingIntent {
        val deepLinkUri = Uri.parse("myapp://deepLink/$destination").buildUpon().build()

        val intent = Intent(Intent.ACTION_VIEW, deepLinkUri).apply {
            setClass(context, MainActivity::class.java)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }

        return PendingIntent.getActivity(
            context,
            destination.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }
}