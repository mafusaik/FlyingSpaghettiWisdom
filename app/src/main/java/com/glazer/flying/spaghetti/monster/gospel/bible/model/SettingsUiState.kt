package com.glazer.flying.spaghetti.monster.gospel.bible.model

import androidx.compose.runtime.Immutable

@Immutable
data class SettingsUiState(
    val isNotificationEnabled: Boolean = false,
    val showPermissionDialog: Boolean = false,
    val selectedLanguage: String = "en",
    val restartRequired: Boolean = false,
    val notificationTime: Pair<Int, Int> = (0 to 0)
)