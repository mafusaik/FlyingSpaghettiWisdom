package com.glazer.flying.spaghetti.monster.gospel.bible.ui.settings

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.glazer.flying.spaghetti.monster.gospel.bible.domain.repository.NotificationRepository
import com.glazer.flying.spaghetti.monster.gospel.bible.domain.repository.PreferencesRepository
import com.glazer.flying.spaghetti.monster.gospel.bible.model.SettingsUiState
import com.glazer.flying.spaghetti.monster.gospel.bible.domain.repository.SettingsRepository
import com.glazer.flying.spaghetti.monster.gospel.bible.model.SettingsEvent
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
        Log.i("SettingsViewModel", "init")
        loadSavedSettings()
    }

    private fun loadSavedSettings() {
      _uiState.update { it.copy(
          isNotificationEnabled = prefRepository.getIsNotificationEnabled(),
          showPermissionDialog = false,
          selectedLanguage = prefRepository.getCurrentLanguage(),
          notificationTime = prefRepository.getNotificationTime()
      ) }
    }

    fun handleEvent(event: SettingsEvent) {
        when (event) {
            is SettingsEvent.EnableNotifications -> {
                Log.i("SETTINGS", "EnableNotifications ${event.hasPermission}, ${event.isEnable}")
                checkPermissionAndToggle(event.hasPermission, event.isEnable)
            }
            is SettingsEvent.SelectLanguage -> {
                prefRepository.setCurrentLanguage(event.language)
                settingRepository.setIsRecreate(true)
                _uiState.update { it.copy(selectedLanguage = event.language, restartRequired = true) }
            }
            is SettingsEvent.ShowPermissionDialog -> {
                _uiState.update { it.copy(showPermissionDialog = event.isShow) }
            }
            is SettingsEvent.ResetRestartFlag -> {
                _uiState.update { it.copy(restartRequired = false) }
            }
            is SettingsEvent.SaveNotificationTime -> {
                prefRepository.setNotificationTime(event.time)
                notificationRepository.cancelAllNotifications()
                viewModelScope.launch {
                    notificationRepository.scheduleNotification(event.time.first, event.time.second)
                }
            }
        }
    }

    private fun checkPermissionAndToggle(hasPermission: Boolean, isEnable: Boolean) {
        Log.i("SETTINGS", "checkPermission hasPermission $hasPermission $isEnable")
        if (hasPermission && isEnable) {
            prefRepository.setIsNotificationEnabled(true)
            _uiState.update {
                it.copy(isNotificationEnabled = true)
            }
        } else if (!hasPermission && isEnable){
            prefRepository.setIsNotificationEnabled(false)
            _uiState.update { it.copy(isNotificationEnabled = false, showPermissionDialog = true) }
        } else {
            prefRepository.setIsNotificationEnabled(false)
            _uiState.update { it.copy(isNotificationEnabled = false, showPermissionDialog = false) }
        }
    }



}