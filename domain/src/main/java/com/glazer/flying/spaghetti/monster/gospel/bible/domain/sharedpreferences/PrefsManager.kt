package com.glazer.flying.spaghetti.monster.gospel.bible.domain.sharedpreferences

interface PrefsManager {

    var recentAdvices: Set<String>

    var savedPage: Int

    var savedOffset: Int

    var currentLanguage: String

    var isNotificationEnabled: Boolean

    var notificationTime: Pair<Int, Int>

    var adviceCount: Int

    var isDialogShowed: Boolean
}