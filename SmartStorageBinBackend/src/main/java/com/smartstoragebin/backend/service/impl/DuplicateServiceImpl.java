package com.smartstoragebin.backend.service.impl;

import com.smartstoragebin.backend.dto.*;
import com.smartstoragebin.backend.entity.FileEntity;
import com.smartstoragebin.backend.entity.enums.DuplicateAction;
import com.smartstoragebin.backend.exception.BadRequestException;
import com.smartstoragebin.backend.exception.ResourceNotFoundException;
import com.smartstoragebin.backend.repository.FileRepository;
import com.smartstoragebin.backend.service.DuplicateService;
import com.smartstoragebin.backend.service.FileService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class DuplicateServiceImpl implements DuplicateService {

    private final FileRepository fileRepository;
    private final FileService fileService;

    public DuplicateServiceImpl(FileRepository fileRepository, FileService fileService) {
        this.fileRepository = fileRepository;
        this.fileService = fileService;
    }

    @Override
    @Transactional(readOnly = true)
    public DuplicateCheckResponse checkForDuplicates(String hash) {
        if (hash == null || hash.trim().isEmpty()) {
            throw new BadRequestException("SHA-256 hash must not be empty");
        }

        List<FileEntity> existing = fileRepository.findByHashAndIsInBinFalse(hash);
        boolean isDuplicate = !existing.isEmpty();
        List<FileDTO> existingDTOs = existing.stream().map(this::mapToDTO).collect(Collectors.toList());
        long wastedSpace = isDuplicate ? existing.get(0).getFileSize() : 0L;

        return DuplicateCheckResponse.builder()
                .isDuplicate(isDuplicate)
                .hash(hash)
                .existingFiles(existingDTOs)
                .wastedSpace(wastedSpace)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DuplicateGroupDTO> getAllDuplicateGroups() {
        List<FileEntity> activeFiles = fileRepository.findByIsInBinFalse();
        Map<String, List<FileEntity>> groupedByHash = activeFiles.stream()
                .filter(f -> f.getHash() != null && !f.getHash().trim().isEmpty())
                .collect(Collectors.groupingBy(FileEntity::getHash));

        List<DuplicateGroupDTO> duplicateGroups = new ArrayList<>();
        for (Map.Entry<String, List<FileEntity>> entry : groupedByHash.entrySet()) {
            if (entry.getValue().size() > 1) {
                List<FileDTO> files = entry.getValue().stream().map(this::mapToDTO).collect(Collectors.toList());
                long wastedSpace = (entry.getValue().size() - 1L) * entry.getValue().get(0).getFileSize();
                duplicateGroups.add(DuplicateGroupDTO.builder()
                        .hash(entry.getKey())
                        .files(files)
                        .totalWastedSpace(wastedSpace)
                        .build());
            }
        }
        return duplicateGroups;
    }

    @Override
    @Transactional(readOnly = true)
    public List<FileDTO> getDuplicatesByHash(String hash) {
        return fileRepository.findByHashAndIsInBinFalse(hash).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public FileDTO handleDuplicateAction(DuplicateActionRequest request) {
        if (request.getAction() == null) {
            throw new BadRequestException("Duplicate action must be specified");
        }

        DuplicateAction action = request.getAction();
        switch (action) {
            case KEEP_EXISTING:
                // Frontend decided not to save the new duplicate; return existing file
                if (request.getTargetFileId() != null) {
                    return fileService.getFileById(request.getTargetFileId());
                } else if (request.getNewFile() != null && request.getNewFile().getHash() != null) {
                    List<FileEntity> existing = fileRepository.findByHashAndIsInBinFalse(request.getNewFile().getHash());
                    if (!existing.isEmpty()) {
                        return mapToDTO(existing.get(0));
                    }
                }
                return null;

            case REPLACE:
                // Replace existing target file with new file details
                if (request.getTargetFileId() == null) {
                    throw new BadRequestException("Target file ID is required for REPLACE action");
                }
                FileEntity existingFile = fileRepository.findByIdAndIsInBinFalse(request.getTargetFileId())
                        .orElseThrow(() -> new ResourceNotFoundException("Target file to replace not found"));

                if (request.getNewFile() != null) {
                    existingFile.setFileName(request.getNewFile().getName());
                    existingFile.setFilePath(request.getNewFile().getPath());
                    existingFile.setFileSize(request.getNewFile().getSize());
                    if (request.getNewFile().getType() != null) {
                        existingFile.setFileType(request.getNewFile().getType());
                    }
                    if (request.getNewFile().getStatus() != null) {
                        existingFile.setStatus(request.getNewFile().getStatus());
                    }
                    if (request.getNewFile().getExpiryDate() != null) {
                        existingFile.setExpiryDate(request.getNewFile().getExpiryDate());
                    }
                    return mapToDTO(fileRepository.save(existingFile));
                }
                return mapToDTO(existingFile);

            case KEEP_BOTH:
                // Register the new file as a distinct entry
                if (request.getNewFile() == null) {
                    throw new BadRequestException("New file details required for KEEP_BOTH action");
                }
                return fileService.createFile(request.getNewFile());

            case CANCEL:
                return null;

            default:
                throw new BadRequestException("Unsupported duplicate action: " + action);
        }
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
