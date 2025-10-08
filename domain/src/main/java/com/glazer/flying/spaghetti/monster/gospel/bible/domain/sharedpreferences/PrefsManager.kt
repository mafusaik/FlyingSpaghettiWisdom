package com.glazer.flying.spaghetti.monster.gospel.bible.domain.sharedpreferences

interface PrefsManager {

    var recentListAdvices: List<String>

    var recentAdvices: Set<String>

    fun addRecentAdvice(advice: String)

    var savedPage: Int

    var savedOffset: Int

    var currentLanguage: String

    var isNotificationEnabled: Boolean

    var notificationTime: Pair<Int, Int>

    var adviceCount: Int

    var isDialogShowed: Boolean
}