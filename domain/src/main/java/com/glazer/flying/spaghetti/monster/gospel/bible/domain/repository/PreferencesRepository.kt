package com.glazer.flying.spaghetti.monster.gospel.bible.domain.repository

import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {

    fun getSavedPage(): Int
    fun setSavedPage(value: Int)

    fun getSavedOffset(): Int
    fun setSavedOffset(value: Int)

    fun getCurrentLanguage(): String
    fun setCurrentLanguage(value: String)

    fun getIsNotificationEnabled(): Boolean
    fun setIsNotificationEnabled(value: Boolean)

    fun getNotificationTime(): Pair<Int, Int>
    fun setNotificationTime(value: Pair<Int, Int>)

    fun getAdviceAmountFlow(): Flow<Int>
    fun setAdviceAmount(value: Int)

    fun getIsDialogShowed(): Boolean
    fun setIsDialogShowed(value: Boolean)
}