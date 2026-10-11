package com.smartstoragebin.backend.service.impl;

import com.smartstoragebin.backend.dto.*;
import com.smartstoragebin.backend.entity.BinFileEntity;
import com.smartstoragebin.backend.entity.FileEntity;
import com.smartstoragebin.backend.entity.enums.CategoryType;
import com.smartstoragebin.backend.entity.enums.FileStatus;
import com.smartstoragebin.backend.exception.BadRequestException;
import com.smartstoragebin.backend.exception.ResourceNotFoundException;
import com.smartstoragebin.backend.repository.BinFileRepository;
import com.smartstoragebin.backend.repository.FileRepository;
import com.smartstoragebin.backend.service.FileService;
import com.smartstoragebin.backend.service.SettingsService;
import com.smartstoragebin.backend.util.CategoryDetector;
import com.smartstoragebin.backend.util.HashUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;
    private final BinFileRepository binFileRepository;
    private final SettingsService settingsService;

    public FileServiceImpl(FileRepository fileRepository, BinFileRepository binFileRepository, SettingsService settingsService) {
        this.fileRepository = fileRepository;
        this.binFileRepository = binFileRepository;
        this.settingsService = settingsService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<FileDTO> getAllFiles() {
        return fileRepository.findByIsInBinFalse().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public FileDTO getFileById(Long id) {
        FileEntity file = fileRepository.findByIdAndIsInBinFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("File not found with id: " + id));
        file.setLastAccessed(LocalDateTime.now());
        fileRepository.save(file);
        return mapToDTO(file);
    }

    @Override
    public FileDTO createFile(CreateFileRequest request) {
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new BadRequestException("File name cannot be empty");
        }
        if (request.getPath() == null || request.getPath().trim().isEmpty()) {
            throw new BadRequestException("File path cannot be empty");
        }

        String fileType = request.getType();
        if (fileType == null || fileType.trim().isEmpty()) {
            fileType = CategoryDetector.detectFileType(request.getName());
        }

        CategoryType category;
        if (request.getCategory() != null && !request.getCategory().trim().isEmpty()) {
            try {
                category = CategoryType.valueOf(request.getCategory().toUpperCase());
            } catch (IllegalArgumentException e) {
                category = CategoryDetector.detectCategory(request.getName(), fileType);
            }
        } else {
            category = CategoryDetector.detectCategory(request.getName(), fileType);
        }

        String hash = request.getHash();
        if (hash == null || hash.trim().isEmpty()) {
            hash = HashUtils.calculateSHA256(request.getName() + ":" + request.getPath() + ":" + request.getSize());
        }

        LocalDate expiryDate = null;
        if (request.getStatus() == FileStatus.TEMPORARY) {
            if (request.getExpiryDate() != null) {
                expiryDate = request.getExpiryDate();
            } else {
                int days = request.getCustomExpiryDays() != null ? request.getCustomExpiryDays() : settingsService.getDefaultExpiryDays();
                expiryDate = LocalDate.now().plusDays(days);
            }
        }

        FileEntity file = FileEntity.builder()
                .fileName(request.getName())
                .filePath(request.getPath())
                .fileSize(request.getSize())
                .fileType(fileType)
                .category(category)
                .status(request.getStatus())
                .hash(hash)
                .expiryDate(expiryDate)
                .isInBin(false)
                .lastAccessed(LocalDateTime.now())
                .build();

        FileEntity saved = fileRepository.save(file);
        return mapToDTO(saved);
    }

    @Override
    public FileDTO updateFile(Long id, UpdateFileRequest request) {
        FileEntity file = fileRepository.findByIdAndIsInBinFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("File not found with id: " + id));

        if (request.getName() != null && !request.getName().trim().isEmpty()) {
            file.setFileName(request.getName());
            file.setFileType(CategoryDetector.detectFileType(request.getName()));
            file.setCategory(CategoryDetector.detectCategory(request.getName(), file.getFileType()));
        }
        if (request.getPath() != null && !request.getPath().trim().isEmpty()) {
            file.setFilePath(request.getPath());
        }
        file.setModifiedAt(LocalDateTime.now());
        return mapToDTO(fileRepository.save(file));
    }

    @Override
    public void moveToBin(Long id) {
        FileEntity file = fileRepository.findByIdAndIsInBinFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("File not found with id: " + id));

        file.setIsInBin(true);
        BinFileEntity binFile = BinFileEntity.builder()
                .file(file)
                .originalPath(file.getFilePath())
                .originalStatus(file.getStatus())
                .originalExpiryDate(file.getExpiryDate())
                .deletedAt(LocalDateTime.now())
                .build();

        binFileRepository.save(binFile);
        fileRepository.save(file);
    }

    @Override
    public FileDTO extendExpiry(Long id, ExtendExpiryRequest request) {
        FileEntity file = fileRepository.findByIdAndIsInBinFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("File not found with id: " + id));

        if (file.getStatus() != FileStatus.TEMPORARY) {
            throw new BadRequestException("Only temporary files can have their expiry extended");
        }

        if (request.getNewExpiryDate() != null) {
            file.setExpiryDate(request.getNewExpiryDate());
        } else if (request.getAdditionalDays() != null && request.getAdditionalDays() > 0) {
            LocalDate baseDate = (file.getExpiryDate() != null && file.getExpiryDate().isAfter(LocalDate.now())) 
                    ? file.getExpiryDate() : LocalDate.now();
            file.setExpiryDate(baseDate.plusDays(request.getAdditionalDays()));
        } else {
            throw new BadRequestException("Please provide additionalDays or newExpiryDate");
        }

        return mapToDTO(fileRepository.save(file));
    }

    @Override
    public FileDTO convertToPermanent(Long id) {
        FileEntity file = fileRepository.findByIdAndIsInBinFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("File not found with id: " + id));

        file.setStatus(FileStatus.PERMANENT);
        file.setExpiryDate(null);
        file.setModifiedAt(LocalDateTime.now());
        return mapToDTO(fileRepository.save(file));
    }

    @Override
    @Transactional(readOnly = true)
    public List<FileDTO> getTemporaryFiles() {
        return fileRepository.findByStatusAndIsInBinFalse(FileStatus.TEMPORARY).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<FileDTO> getPermanentFiles() {
        return fileRepository.findByStatusAndIsInBinFalse(FileStatus.PERMANENT).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<FileDTO> getExpiredFiles() {
        return fileRepository.findByExpiryDateLessThanEqualAndStatusAndIsInBinFalse(LocalDate.now(), FileStatus.TEMPORARY)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<FileDTO> searchFiles(String query, String category, String status, Long minSize, Long maxSize, LocalDate createdDate, LocalDate expiryDate) {
        return fileRepository.findByIsInBinFalse().stream()
                .filter(file -> {
                    if (query != null && !query.trim().isEmpty()) {
                        String q = query.toLowerCase();
                        boolean nameMatch = file.getFileName().toLowerCase().contains(q);
                        boolean typeMatch = file.getFileType() != null && file.getFileType().toLowerCase().contains(q);
                        if (!nameMatch && !typeMatch) return false;
                    }
                    if (category != null && !category.trim().isEmpty() && !category.equalsIgnoreCase("ALL")) {
                        if (file.getCategory() == null || !file.getCategory().name().equalsIgnoreCase(category)) {
                            return false;
                        }
                    }
                    if (status != null && !status.trim().isEmpty() && !status.equalsIgnoreCase("ALL")) {
                        if (status.equalsIgnoreCase("EXPIRED")) {
                            if (file.getStatus() != FileStatus.TEMPORARY || !file.isExpired()) return false;
                        } else if (status.equalsIgnoreCase("EXPIRING_SOON")) {
                            if (file.getStatus() != FileStatus.TEMPORARY || !file.isExpiringSoon(settingsService.getExpiryWarningDays())) return false;
                        } else if (file.getStatus() == null || !file.getStatus().name().equalsIgnoreCase(status)) {
                            return false;
                        }
                    }
                    if (minSize != null && file.getFileSize() < minSize) return false;
                    if (maxSize != null && file.getFileSize() > maxSize) return false;
                    if (createdDate != null && file.getCreatedAt() != null && !file.getCreatedAt().toLocalDate().isEqual(createdDate)) return false;
                    if (expiryDate != null && (file.getExpiryDate() == null || !file.getExpiryDate().isEqual(expiryDate))) return false;

                    return true;
                })
                .map(this::mapToDTO)
                .collect(Collectors.toList());
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
