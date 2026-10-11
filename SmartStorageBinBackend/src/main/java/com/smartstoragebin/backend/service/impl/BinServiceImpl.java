package com.smartstoragebin.backend.service.impl;

import com.smartstoragebin.backend.dto.BinFileDTO;
import com.smartstoragebin.backend.dto.FileDTO;
import com.smartstoragebin.backend.entity.BinFileEntity;
import com.smartstoragebin.backend.entity.FileEntity;
import com.smartstoragebin.backend.exception.ResourceNotFoundException;
import com.smartstoragebin.backend.repository.BinFileRepository;
import com.smartstoragebin.backend.repository.FileRepository;
import com.smartstoragebin.backend.service.BinService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class BinServiceImpl implements BinService {

    private final BinFileRepository binFileRepository;
    private final FileRepository fileRepository;

    public BinServiceImpl(BinFileRepository binFileRepository, FileRepository fileRepository) {
        this.binFileRepository = binFileRepository;
        this.fileRepository = fileRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<BinFileDTO> getBinFiles() {
        return binFileRepository.findAllByOrderByDeletedAtDesc().stream()
                .map(this::mapToBinDTO)
                .collect(Collectors.toList());
    }

    @Override
    public FileDTO restoreFromBin(Long binId) {
        // Find by bin entry ID first, or fallback to file ID
        BinFileEntity binFile = binFileRepository.findById(binId)
                .or(() -> binFileRepository.findByFileId(binId))
                .orElseThrow(() -> new ResourceNotFoundException("Recycle bin entry not found with id: " + binId));

        FileEntity file = binFile.getFile();
        if (file == null) {
            throw new ResourceNotFoundException("Associated file not found");
        }

        // Critical requirement: restore original lifecycle status and expiry date
        file.setIsInBin(false);
        file.setStatus(binFile.getOriginalStatus());
        file.setExpiryDate(binFile.getOriginalExpiryDate());
        file.setFilePath(binFile.getOriginalPath());

        binFileRepository.delete(binFile);
        FileEntity saved = fileRepository.save(file);

        return FileDTO.builder()
                .id(saved.getId())
                .name(saved.getFileName())
                .path(saved.getFilePath())
                .size(saved.getFileSize())
                .type(saved.getFileType())
                .category(saved.getCategory() != null ? saved.getCategory().name() : null)
                .status(saved.getStatus() != null ? saved.getStatus().name() : null)
                .createdAt(saved.getCreatedAt() != null ? saved.getCreatedAt().toString() : null)
                .expiryDate(saved.getExpiryDate() != null ? saved.getExpiryDate().toString() : null)
                .hash(saved.getHash())
                .isInBin(false)
                .build();
    }

    @Override
    public void deletePermanently(Long binId) {
        BinFileEntity binFile = binFileRepository.findById(binId)
                .or(() -> binFileRepository.findByFileId(binId))
                .orElseThrow(() -> new ResourceNotFoundException("Recycle bin entry not found with id: " + binId));

        FileEntity file = binFile.getFile();
        binFileRepository.delete(binFile);
        if (file != null) {
            fileRepository.delete(file);
        }
    }

    @Override
    public void emptyBin() {
        List<BinFileEntity> allBinFiles = binFileRepository.findAll();
        for (BinFileEntity binFile : allBinFiles) {
            FileEntity file = binFile.getFile();
            binFileRepository.delete(binFile);
            if (file != null) {
                fileRepository.delete(file);
            }
        }
    }

    private BinFileDTO mapToBinDTO(BinFileEntity entity) {
        FileEntity file = entity.getFile();
        return BinFileDTO.builder()
                .id(entity.getId())
                .fileId(file != null ? file.getId() : null)
                .name(file != null ? file.getFileName() : "Unknown")
                .originalPath(entity.getOriginalPath())
                .size(file != null ? file.getFileSize() : 0L)
                .type(file != null ? file.getFileType() : "Unknown")
                .originalStatus(entity.getOriginalStatus() != null ? entity.getOriginalStatus().name() : "TEMPORARY")
                .deletedAt(entity.getDeletedAt() != null ? entity.getDeletedAt().toString() : null)
                .originalExpiryDate(entity.getOriginalExpiryDate() != null ? entity.getOriginalExpiryDate().toString() : null)
                .build();
    }
}
