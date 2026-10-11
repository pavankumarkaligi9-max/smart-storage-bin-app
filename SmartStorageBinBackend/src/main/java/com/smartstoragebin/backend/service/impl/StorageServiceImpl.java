package com.smartstoragebin.backend.service.impl;

import com.smartstoragebin.backend.dto.FileDTO;
import com.smartstoragebin.backend.dto.StorageResponse;
import com.smartstoragebin.backend.dto.TemporaryDashboardResponse;
import com.smartstoragebin.backend.entity.BinFileEntity;
import com.smartstoragebin.backend.entity.FileEntity;
import com.smartstoragebin.backend.entity.enums.FileStatus;
import com.smartstoragebin.backend.repository.BinFileRepository;
import com.smartstoragebin.backend.repository.FileRepository;
import com.smartstoragebin.backend.service.SettingsService;
import com.smartstoragebin.backend.service.StorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class StorageServiceImpl implements StorageService {

    private final FileRepository fileRepository;
    private final BinFileRepository binFileRepository;
    private final SettingsService settingsService;

    @Value("${app.storage.total-bytes:137438953472}") // Default 128 GB
    private Long totalStorage;

    public StorageServiceImpl(FileRepository fileRepository, BinFileRepository binFileRepository, SettingsService settingsService) {
        this.fileRepository = fileRepository;
        this.binFileRepository = binFileRepository;
        this.settingsService = settingsService;
    }

    @Override
    public StorageResponse getStorageInfo() {
        List<FileEntity> activeFiles = fileRepository.findByIsInBinFalse();

        long usedStorage = activeFiles.stream().mapToLong(FileEntity::getFileSize).sum();
        long freeStorage = Math.max(0, totalStorage - usedStorage);

        long temporaryStorage = activeFiles.stream()
                .filter(f -> f.getStatus() == FileStatus.TEMPORARY)
                .mapToLong(FileEntity::getFileSize)
                .sum();

        // Calculate duplicate storage (storage wasted by duplicate copies)
        Map<String, List<FileEntity>> filesByHash = activeFiles.stream()
                .collect(Collectors.groupingBy(FileEntity::getHash));
        
        long duplicateStorage = filesByHash.values().stream()
                .filter(group -> group.size() > 1)
                .mapToLong(group -> (group.size() - 1L) * group.get(0).getFileSize())
                .sum();

        // Calculate bin storage
        List<BinFileEntity> binFiles = binFileRepository.findAll();
        long binStorage = binFiles.stream()
                .filter(b -> b.getFile() != null)
                .mapToLong(b -> b.getFile().getFileSize())
                .sum();

        return StorageResponse.builder()
                .totalStorage(totalStorage)
                .usedStorage(usedStorage)
                .freeStorage(freeStorage)
                .temporaryStorage(temporaryStorage)
                .duplicateStorage(duplicateStorage)
                .binStorage(binStorage)
                .build();
    }

    @Override
    public TemporaryDashboardResponse getTemporaryDashboard() {
        List<FileEntity> temporaryFiles = fileRepository.findByStatusAndIsInBinFalse(FileStatus.TEMPORARY);
        int warningDays = settingsService.getExpiryWarningDays();
        LocalDate today = LocalDate.now();

        int totalTemporaryFiles = temporaryFiles.size();
        long totalTemporaryStorage = temporaryFiles.stream().mapToLong(FileEntity::getFileSize).sum();

        int expiredCount = 0;
        int expiringTodayCount = 0;
        int expiringSoonCount = 0;

        for (FileEntity file : temporaryFiles) {
            LocalDate expiry = file.getExpiryDate();
            if (expiry != null) {
                if (expiry.isBefore(today)) {
                    expiredCount++;
                } else if (expiry.isEqual(today)) {
                    expiringTodayCount++;
                } else if (file.isExpiringSoon(warningDays)) {
                    expiringSoonCount++;
                }
            }
        }

        List<FileDTO> largestTemporaryFiles = temporaryFiles.stream()
                .sorted(Comparator.comparing(FileEntity::getFileSize).reversed())
                .limit(5)
                .map(this::mapToDTO)
                .collect(Collectors.toList());

        return TemporaryDashboardResponse.builder()
                .totalTemporaryFiles(totalTemporaryFiles)
                .totalTemporaryStorage(totalTemporaryStorage)
                .expiredCount(expiredCount)
                .expiringTodayCount(expiringTodayCount)
                .expiringSoonCount(expiringSoonCount)
                .largestTemporaryFiles(largestTemporaryFiles)
                .build();
    }

    private FileDTO mapToDTO(FileEntity entity) {
        if (entity == null) return null;
        return FileDTO.builder()
                .id(entity.getId())
                .name(entity.getFileName())
                .path(entity.getFilePath())
                .size(entity.getFileSize())
                .type(entity.getFileType())
                .category(entity.getCategory() != null ? entity.getCategory().name() : null)
                .status(entity.getStatus() != null ? entity.getStatus().name() : null)
                .createdAt(entity.getCreatedAt() != null ? entity.getCreatedAt().toString() : null)
                .expiryDate(entity.getExpiryDate() != null ? entity.getExpiryDate().toString() : null)
                .hash(entity.getHash())
                .isInBin(entity.getIsInBin() != null ? entity.getIsInBin() : false)
                .build();
    }
}
