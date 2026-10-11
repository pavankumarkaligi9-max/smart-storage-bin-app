package com.smartstoragebin.backend.entity;

import com.smartstoragebin.backend.entity.enums.CategoryType;
import com.smartstoragebin.backend.entity.enums.FileStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "files")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false, unique = true)
    private String filePath;

    @Column(nullable = false)
    private Long fileSize;

    private String fileType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CategoryType category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FileStatus status;

    @Column(nullable = false)
    private String hash;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime modifiedAt;

    private LocalDate expiryDate;

    private LocalDateTime lastAccessed;

    @Column(nullable = false)
    @Builder.Default
    private Boolean isInBin = false;

    public boolean isExpired() {
        if (expiryDate == null) return false;
        return LocalDate.now().isAfter(expiryDate);
    }

    public boolean isExpiringSoon(int warningDays) {
        if (expiryDate == null) return false;
        long daysBetween = ChronoUnit.DAYS.between(LocalDate.now(), expiryDate);
        return daysBetween >= 0 && daysBetween <= warningDays;
    }

    public Long getRemainingDays() {
        if (expiryDate == null) return null;
        return ChronoUnit.DAYS.between(LocalDate.now(), expiryDate);
    }
}
