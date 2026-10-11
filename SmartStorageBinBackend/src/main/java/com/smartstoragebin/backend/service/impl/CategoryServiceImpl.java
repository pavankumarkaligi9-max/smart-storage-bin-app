package com.smartstoragebin.backend.service.impl;

import com.smartstoragebin.backend.dto.CategoryResponse;
import com.smartstoragebin.backend.dto.FileDTO;
import com.smartstoragebin.backend.entity.FileEntity;
import com.smartstoragebin.backend.entity.enums.CategoryType;
import com.smartstoragebin.backend.exception.BadRequestException;
import com.smartstoragebin.backend.repository.FileRepository;
import com.smartstoragebin.backend.service.CategoryService;
import com.smartstoragebin.backend.util.CategoryDetector;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {

    private final FileRepository fileRepository;

    public CategoryServiceImpl(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @Override
    public List<CategoryResponse> getAllCategories() {
        List<FileEntity> activeFiles = fileRepository.findByIsInBinFalse();
        Map<CategoryType, List<FileEntity>> filesByCategory = activeFiles.stream()
                .filter(f -> f.getCategory() != null)
                .collect(Collectors.groupingBy(FileEntity::getCategory));

        List<CategoryResponse> categories = new ArrayList<>();
        for (CategoryType type : CategoryType.values()) {
            List<FileEntity> files = filesByCategory.getOrDefault(type, List.of());
            int count = files.size();
            long totalSize = files.stream().mapToLong(FileEntity::getFileSize).sum();
            String icon = CategoryDetector.getCategoryIcon(type);

            categories.add(CategoryResponse.builder()
                    .name(capitalize(type.name()))
                    .fileCount(count)
                    .totalSize(totalSize)
                    .icon(icon)
                    .build());
        }
        return categories;
    }

    @Override
    public List<FileDTO> getFilesByCategory(String categoryName) {
        if (categoryName == null || categoryName.trim().isEmpty()) {
            throw new BadRequestException("Category name must not be empty");
        }

        CategoryType categoryType;
        try {
            categoryType = CategoryType.valueOf(categoryName.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid category: " + categoryName);
        }

        return fileRepository.findByCategoryAndIsInBinFalse(categoryType).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
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
