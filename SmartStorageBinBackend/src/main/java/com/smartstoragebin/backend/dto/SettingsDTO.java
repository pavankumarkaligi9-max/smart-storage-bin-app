package com.smartstoragebin.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SettingsDTO {
    private Boolean notificationEnabled;
    private Integer defaultExpiryDays;
    private Integer expiryWarningDays;
    private Integer binRetentionDays;
    private Boolean duplicateDetectionEnabled;
}
