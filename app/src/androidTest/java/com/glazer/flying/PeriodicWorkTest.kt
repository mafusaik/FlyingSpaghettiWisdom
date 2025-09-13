package com.glazer.flying

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.testing.WorkManagerTestInitHelper
import com.glazer.flying.spaghetti.monster.gospel.bible.utils.Constants
import com.glazer.flying.spaghetti.monster.gospel.bible.workmanager.NotificationWorker
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit


@RunWith(AndroidJUnit4::class)
class PeriodicWorkTest {

//    @get:Rule
//    var instantTaskExecutorRule = InstantTaskExecutorRule()
//    @get:Rule
//    val wmRule = WorkManagerTestRule()

//    @Test
//    @Throws(Exception::class)
//    fun testAppliesBlur() {
//        val inputData = Data.Builder()
//        .putString(Constants.KEY_MESSAGE, "message")
//        .build()
//
//        val request = OneTimeWorkRequestBuilder<NotificationWorker>()
//            .setInputData(inputData)
//            .build()
//
//        val context = wmRule.targetContext
//        val workManager = WorkManager.getInstance(context)
//        val testDriver = WorkManagerTestInitHelper.getTestDriver(context)
//
//        workManager.enqueue(request).result
//        testDriver?.setAllConstraintsMet(request.id)
//
//        val workInfo = workManager.getWorkInfoById(request.id).get()
//        val outputData = workInfo?.outputData?.getString(Constants.KEY_MESSAGE)
//
//        assertEquals("message", outputData)
//        assertEquals(WorkInfo.State.SUCCEEDED, workInfo?.state)
//    }
//
//    @Test
//    @Throws(Exception::class)
//    fun testPeriodicWork() {
//        val context = wmRule.targetContext
//        val testMessage = "Periodic Test Message"
//        val input = Data.Builder()
//            .putString(Constants.KEY_MESSAGE, testMessage)
//            .build()
//
//        val request = PeriodicWorkRequestBuilder<NotificationWorker>(1, TimeUnit.DAYS)
//            .setInputData(input)
//            .setInitialDelay(80000, TimeUnit.MILLISECONDS)
//            .build()
//
//        val workManager = WorkManager.getInstance(context)
//        val testDriver = WorkManagerTestInitHelper.getTestDriver(context)
//        workManager.enqueue(request).result.get()
//        testDriver?.setPeriodDelayMet(request.id)
//        val workInfo = workManager.getWorkInfoById(request.id).get()
//
//        val outputData = request.workSpec.input.getString(Constants.KEY_MESSAGE)
//        // Assert
//        assertEquals(workInfo?.state, WorkInfo.State.ENQUEUED)
//        assertEquals(testMessage, outputData)
//    }
}

