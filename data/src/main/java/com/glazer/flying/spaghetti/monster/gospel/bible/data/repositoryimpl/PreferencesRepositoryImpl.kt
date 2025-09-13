package com.glazer.flying.spaghetti.monster.gospel.bible.data.repositoryimpl

import com.glazer.flying.spaghetti.monster.gospel.bible.domain.repository.PreferencesRepository
import com.glazer.flying.spaghetti.monster.gospel.bible.domain.sharedpreferences.PrefsManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class PreferencesRepositoryImpl @Inject constructor(
   private val prefManager: PrefsManager
) : PreferencesRepository {

    private val adviceCountFlow = MutableStateFlow(prefManager.adviceCount)

    override fun getCurrentLanguage(): String {
        return prefManager.currentLanguage
    }

    override fun setCurrentLanguage(value: String) {
        prefManager.currentLanguage = value
    }

    override fun getIsNotificationEnabled(): Boolean {
       return prefManager.isNotificationEnabled
    }

    override fun setIsNotificationEnabled(value: Boolean) {
        prefManager.isNotificationEnabled = value
    }

    override fun getNotificationTime(): Pair<Int, Int> {
       return prefManager.notificationTime
    }

    override fun setNotificationTime(value: Pair<Int, Int>) {
        prefManager.notificationTime = value
    }

    override fun getAdviceAmountFlow(): Flow<Int> {
        return adviceCountFlow.asStateFlow()
    }

    override fun setAdviceAmount(value: Int) {
        prefManager.adviceCount = value
        adviceCountFlow.value = value
    }

    override fun getIsDialogShowed(): Boolean {
       return prefManager.isDialogShowed
    }

    override fun setIsDialogShowed(value: Boolean) {
     prefManager.isDialogShowed = value
    }

    override fun getSavedPage(): Int {
        return prefManager.savedPage
    }

    override fun setSavedPage(value: Int) {
        prefManager.savedPage = value
    }

    override fun getSavedOffset(): Int {
        return prefManager.savedOffset
    }

    override fun setSavedOffset(value: Int) {
        prefManager.savedOffset = value
    }
}