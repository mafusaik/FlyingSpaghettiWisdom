package com.glazer.flying.spaghetti.monster.gospel.bible.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.glazer.flying.spaghetti.monster.gospel.bible.domain.repository.NotificationRepository
import com.glazer.flying.spaghetti.monster.gospel.bible.domain.repository.PreferencesRepository
import com.glazer.flying.spaghetti.monster.gospel.bible.domain.repository.SettingsRepository
import com.glazer.flying.spaghetti.monster.gospel.bible.model.SettingsEvent
import com.glazer.flying.spaghetti.monster.gospel.bible.model.SettingsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingRepository: SettingsRepository,
    private val prefRepository: PreferencesRepository,
    private val notificationRepository: NotificationRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        loadSavedSettings()
    }

    private fun loadSavedSettings() {
        _uiState.update {
            it.copy(
                isNotificationEnabled = prefRepository.getIsNotificationEnabled(),
                showPermissionDialog = false,
                selectedLanguage = prefRepository.getCurrentLanguage(),
                notificationTime = prefRepository.getNotificationTime().checkDefault()
            )
        }
    }

    fun handleEvent(event: SettingsEvent) {
        when (event) {
            is SettingsEvent.EnableNotifications -> {
                checkPermissionAndToggle(event.hasPermission, event.isEnable)
                setNotification(event.hasPermission, event.isEnable)
            }

            is SettingsEvent.SelectLanguage -> {
                prefRepository.setCurrentLanguage(event.language)
                settingRepository.setIsRecreate(true)
                _uiState.update {
                    it.copy(
                        selectedLanguage = event.language,
                        restartRequired = true
                    )
                }
            }

            is SettingsEvent.ShowPermissionDialog -> {
                _uiState.update { it.copy(showPermissionDialog = event.isShow) }
            }

            is SettingsEvent.ResetRestartFlag -> {
                _uiState.update { it.copy(restartRequired = false) }
            }

            is SettingsEvent.SaveNotificationTime -> {
                prefRepository.setNotificationTime(event.time)
                cancelWork()
                startWork(event.time)
            }
        }
    }

    private fun setNotification(hasPermission: Boolean, isEnable: Boolean) {
        if (hasPermission && isEnable) {
            startWork(prefRepository.getNotificationTime())
        } else {
            cancelWork()
        }
    }

    private fun startWork(time: Pair<Int, Int>) {
        val hour = time.first
        val minute = time.second
        if (hour < 0 || minute < 0) return

        viewModelScope.launch {
            notificationRepository.scheduleNotification(hour, minute)
        }
    }

    private fun cancelWork() {
        notificationRepository.cancelNotificationWork()
    }

    private fun checkPermissionAndToggle(hasPermission: Boolean, isEnable: Boolean) {
        if (hasPermission && isEnable) {
            prefRepository.setIsNotificationEnabled(true)
            _uiState.update {
                it.copy(isNotificationEnabled = true)
            }
        } else if (!hasPermission && isEnable) {
            prefRepository.setIsNotificationEnabled(false)
            _uiState.update { it.copy(isNotificationEnabled = false, showPermissionDialog = true) }
        } else {
            prefRepository.setIsNotificationEnabled(false)
            _uiState.update { it.copy(isNotificationEnabled = false, showPermissionDialog = false) }
        }
    }

//    private suspend fun getCountryFromIP(): String? {
//        return try {
//            val country = withContext(Dispatchers.IO) {
//                URL("https://api.country.is/").readText()
//            }
//            JSONObject(country).getString("country")
//        } catch (e: Exception) {
//            null
//        }
//    }

    private fun Pair<Int, Int>.checkDefault(): Pair<Int, Int> {
        return if (first < 0 || second < 0) 0 to 0
        else this
    }
}


