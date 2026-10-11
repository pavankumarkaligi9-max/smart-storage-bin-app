package com.smartstoragebin.ui.screens.settings

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class SettingsState(
    val notificationsEnabled: Boolean = true,
    val expiryWarningsEnabled: Boolean = true,
    val defaultTemporaryDurationDays: Int = 7,
    val expiryWarningPeriodDays: Int = 3,
    val duplicateDetectionEnabled: Boolean = true,
    val autoDeleteBinEnabled: Boolean = true,
    val theme: String = "System Default"
)

class SettingsViewModel : ViewModel() {
    // TODO: Load and save from DataStore Preferences
    
    private val _settingsState = MutableStateFlow(SettingsState())
    val settingsState: StateFlow<SettingsState> = _settingsState.asStateFlow()

    fun setNotificationsEnabled(enabled: Boolean) {
        _settingsState.update { it.copy(notificationsEnabled = enabled) }
    }

    fun setExpiryWarningsEnabled(enabled: Boolean) {
        _settingsState.update { it.copy(expiryWarningsEnabled = enabled) }
    }
    
    fun setDefaultTemporaryDurationDays(days: Int) {
        _settingsState.update { it.copy(defaultTemporaryDurationDays = days) }
    }
    
    fun setExpiryWarningPeriodDays(days: Int) {
        _settingsState.update { it.copy(expiryWarningPeriodDays = days) }
    }

    fun setDuplicateDetectionEnabled(enabled: Boolean) {
        _settingsState.update { it.copy(duplicateDetectionEnabled = enabled) }
    }

    fun setAutoDeleteBinEnabled(enabled: Boolean) {
        _settingsState.update { it.copy(autoDeleteBinEnabled = enabled) }
    }
    
    fun setTheme(theme: String) {
        _settingsState.update { it.copy(theme = theme) }
    }
}
