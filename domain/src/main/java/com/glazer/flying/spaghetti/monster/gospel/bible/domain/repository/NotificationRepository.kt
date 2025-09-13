package com.glazer.flying.spaghetti.monster.gospel.bible.domain.repository

interface NotificationRepository {
    suspend fun scheduleNotification(hour: Int, minute: Int)
    fun cancelAllNotifications()
}