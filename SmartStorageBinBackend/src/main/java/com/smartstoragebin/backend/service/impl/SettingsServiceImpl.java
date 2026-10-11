package com.smartstoragebin.backend.service.impl;

import com.smartstoragebin.backend.dto.SettingsDTO;
import com.smartstoragebin.backend.entity.SettingsEntity;
import com.smartstoragebin.backend.repository.SettingsRepository;
import com.smartstoragebin.backend.service.SettingsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SettingsServiceImpl implements SettingsService {

    private final SettingsRepository settingsRepository;

    @Value("${app.temporary.default-days:7}")
    private int configDefaultExpiryDays;

    @Value("${app.expiry.warning-days:3}")
    private int configExpiryWarningDays;

    @Value("${app.bin.retention-days:30}")
    private int configBinRetentionDays;

    public SettingsServiceImpl(SettingsRepository settingsRepository) {
        this.settingsRepository = settingsRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public SettingsDTO getSettings() {
        SettingsEntity entity = getOrCreateSettings();
        return mapToDTO(entity);
    }

    @Override
    public SettingsDTO updateSettings(SettingsDTO dto) {
        SettingsEntity entity = getOrCreateSettings();

        if (dto.getNotificationEnabled() != null) {
            entity.setNotificationEnabled(dto.getNotificationEnabled());
        }
        if (dto.getDefaultExpiryDays() != null && dto.getDefaultExpiryDays() > 0) {
            entity.setDefaultExpiryDays(dto.getDefaultExpiryDays());
        }
        if (dto.getExpiryWarningDays() != null && dto.getExpiryWarningDays() > 0) {
            entity.setExpiryWarningDays(dto.getExpiryWarningDays());
        }
        if (dto.getBinRetentionDays() != null && dto.getBinRetentionDays() > 0) {
            entity.setBinRetentionDays(dto.getBinRetentionDays());
        }
        if (dto.getDuplicateDetectionEnabled() != null) {
            entity.setDuplicateDetectionEnabled(dto.getDuplicateDetectionEnabled());
        }

        SettingsEntity saved = settingsRepository.save(entity);
        return mapToDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public int getDefaultExpiryDays() {
        return settingsRepository.findFirstByOrderByIdAsc()
                .map(SettingsEntity::getDefaultExpiryDays)
                .orElse(configDefaultExpiryDays);
    }

    @Override
    @Transactional(readOnly = true)
    public int getExpiryWarningDays() {
        return settingsRepository.findFirstByOrderByIdAsc()
                .map(SettingsEntity::getExpiryWarningDays)
                .orElse(configExpiryWarningDays);
    }

    private SettingsEntity getOrCreateSettings() {
        return settingsRepository.findFirstByOrderByIdAsc().orElseGet(() -> {
            SettingsEntity defaultSettings = SettingsEntity.builder()
                    .notificationEnabled(true)
                    .defaultExpiryDays(configDefaultExpiryDays)
                    .expiryWarningDays(configExpiryWarningDays)
                    .binRetentionDays(configBinRetentionDays)
                    .duplicateDetectionEnabled(true)
                    .build();
            return settingsRepository.save(defaultSettings);
        });
    }

    private SettingsDTO mapToDTO(SettingsEntity entity) {
        return SettingsDTO.builder()
                .notificationEnabled(entity.getNotificationEnabled())
                .defaultExpiryDays(entity.getDefaultExpiryDays())
                .expiryWarningDays(entity.getExpiryWarningDays())
                .binRetentionDays(entity.getBinRetentionDays())
                .duplicateDetectionEnabled(entity.getDuplicateDetectionEnabled())
                .build();
    }
}
