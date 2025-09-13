package com.glazer.flying.spaghetti.monster.gospel.bible.model

import androidx.compose.runtime.Immutable

@Immutable
sealed class SettingsEvent {
    @Immutable
    data class EnableNotifications(val hasPermission: Boolean, val isEnable: Boolean) :
        SettingsEvent()

    @Immutable
    data class ShowPermissionDialog(val isShow: Boolean) : SettingsEvent()

    @Immutable
    data class SelectLanguage(val language: String) : SettingsEvent()

    @Immutable
    data class SaveNotificationTime(val time: Pair<Int, Int>) : SettingsEvent()
    data object ResetRestartFlag : SettingsEvent()
}