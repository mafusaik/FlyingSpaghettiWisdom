package com.glazer.flying.spaghetti.monster.gospel.bible.data.sharedpreferences

import android.content.Context
import com.glazer.flying.spaghetti.monster.gospel.bible.domain.constants.AppConstants.KEY_ADVICE_COUNT
import com.glazer.flying.spaghetti.monster.gospel.bible.domain.constants.AppConstants.KEY_DIALOG
import com.glazer.flying.spaghetti.monster.gospel.bible.domain.constants.AppConstants.KEY_HOURS
import com.glazer.flying.spaghetti.monster.gospel.bible.domain.constants.AppConstants.KEY_LANGUAGE
import com.glazer.flying.spaghetti.monster.gospel.bible.domain.constants.AppConstants.KEY_MINUTES
import com.glazer.flying.spaghetti.monster.gospel.bible.domain.constants.AppConstants.KEY_NOTIFICATIONS
import com.glazer.flying.spaghetti.monster.gospel.bible.domain.constants.AppConstants.KEY_RECENT_ADVICE
import com.glazer.flying.spaghetti.monster.gospel.bible.domain.constants.AppConstants.KEY_SAVED_OFFSET
import com.glazer.flying.spaghetti.monster.gospel.bible.domain.constants.AppConstants.KEY_SAVED_PAGE
import com.glazer.flying.spaghetti.monster.gospel.bible.domain.constants.AppConstants.PREFERENCES_NAME_TOKEN
import com.glazer.flying.spaghetti.monster.gospel.bible.domain.sharedpreferences.PrefsManager
import java.util.Locale
import javax.inject.Inject

internal class PrefsManagerImpl @Inject constructor(
    val context: Context
) : PrefsManager {
    private val prefs =
        context.getSharedPreferences(PREFERENCES_NAME_TOKEN, Context.MODE_PRIVATE)

    override var recentAdvices: Set<String>
        get() = prefs.getStringSet(KEY_RECENT_ADVICE, emptySet()) ?: emptySet()
        set(value) {
            prefs.edit().putStringSet(KEY_RECENT_ADVICE, value).apply()
        }

    override var savedPage: Int
        get() = prefs.getInt(KEY_SAVED_PAGE, 0)
        set(value) {
            prefs.edit().putInt(KEY_SAVED_PAGE, value).apply()
        }

    override var savedOffset: Int
        get() = prefs.getInt(KEY_SAVED_OFFSET, 0)
        set(value) {
            prefs.edit().putInt(KEY_SAVED_OFFSET, value).apply()
        }

    override var currentLanguage: String
        get() = prefs.getString(KEY_LANGUAGE, Locale.getDefault().language) ?: "en"
        set(value) {
            prefs.edit().putString(KEY_LANGUAGE, value).apply()
        }

    override var isNotificationEnabled: Boolean
        get() = prefs.getBoolean(KEY_NOTIFICATIONS, false)
        set(value) {
            prefs.edit().putBoolean(KEY_NOTIFICATIONS, value).apply()
        }

    override var notificationTime: Pair<Int, Int>
        get() = prefs.getInt(KEY_HOURS, 0) to prefs.getInt(KEY_MINUTES, 0)
        set(value) {
            prefs.edit()
                .putInt(KEY_HOURS, value.first)
                .putInt(KEY_MINUTES, value.second)
                .apply()
        }

    override var adviceCount: Int
        get() = prefs.getInt(KEY_ADVICE_COUNT, 0)
        set(value) {
            prefs.edit().putInt(KEY_ADVICE_COUNT, value).apply()
        }

    override var isDialogShowed: Boolean
        get() = prefs.getBoolean(KEY_DIALOG, false)
        set(value) {
            prefs.edit().putBoolean(KEY_DIALOG, value).apply()
        }
}