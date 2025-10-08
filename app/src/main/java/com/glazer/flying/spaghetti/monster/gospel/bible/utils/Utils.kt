package com.glazer.flying.spaghetti.monster.gospel.bible.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay

@Composable
fun rememberThrottleClick(delayMillis: Long = 1000L): (onClick: () -> Unit) -> Unit {
    var enabled by remember { mutableStateOf(true) }

    LaunchedEffect(enabled) {
        if (!enabled) {
            delay(delayMillis)
            enabled = true
        }
    }

    return { onClick ->
        if (enabled) {
            enabled = false
            onClick()
        }
    }
}