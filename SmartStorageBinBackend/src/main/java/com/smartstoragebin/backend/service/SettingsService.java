package com.smartstoragebin.backend.service;

import com.smartstoragebin.backend.dto.*;

public interface SettingsService {
    SettingsDTO getSettings();
    SettingsDTO updateSettings(SettingsDTO dto);
    int getDefaultExpiryDays();
    int getExpiryWarningDays();
}
