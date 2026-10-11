package com.smartstoragebin.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "settings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SettingsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Builder.Default
    private Boolean notificationEnabled = true;

    @Column(nullable = false)
    @Builder.Default
    private Integer defaultExpiryDays = 7;

    @Column(nullable = false)
    @Builder.Default
    private Integer expiryWarningDays = 3;

    @Column(nullable = false)
    @Builder.Default
    private Integer binRetentionDays = 30;

    @Column(nullable = false)
    @Builder.Default
    private Boolean duplicateDetectionEnabled = true;
}
