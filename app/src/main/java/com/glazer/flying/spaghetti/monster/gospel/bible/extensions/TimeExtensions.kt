package com.glazer.flying.spaghetti.monster.gospel.bible.extensions

import java.time.Duration
import java.time.LocalDateTime

fun calculateDelay(hour: Int, minute: Int): Long {
    val now = LocalDateTime.now()
    var scheduledTime = now.withHour(hour).withMinute(minute).withSecond(0).withNano(0)
    if (scheduledTime.isBefore(now)) {
        scheduledTime = scheduledTime.plusDays(1)
    }
    return Duration.between(now, scheduledTime).toMillis()
}