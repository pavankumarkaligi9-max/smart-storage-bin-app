package com.smartstoragebin.backend.dto;

import com.smartstoragebin.backend.entity.FileEntity;
import com.smartstoragebin.backend.entity.BinFileEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileDTO {
    private Long id;
    private String name;
    private String path;
    private Long size;
    private String type;
    private String category;
    private String status;
    private String createdAt;
    private String expiryDate;
    private String hash;
    private Boolean isInBin;
    private String deletedAt;
    private String originalPath;

    public static FileDTO fromEntity(FileEntity entity) {
        if (entity == null) {
            return null;
        }
        return FileDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .path(entity.getPath())
                .size(entity.getSize())
                .type(entity.getType())
                .category(entity.getCategory() != null ? entity.getCategory().name() : null)
                .status(entity.getStatus() != null ? entity.getStatus().name() : null)
                .createdAt(entity.getCreatedAt() != null ? entity.getCreatedAt().toString() : null)
                .expiryDate(entity.getExpiryDate() != null ? entity.getExpiryDate().toString() : null)
                .hash(entity.getHash())
                .isInBin(false) // default assumption, override if bin entity provided
                .build();
    }

    public static FileDTO fromEntity(FileEntity entity, BinFileEntity binEntity) {
        FileDTO dto = fromEntity(entity);
        if (dto != null && binEntity != null) {
            dto.setIsInBin(true);
            dto.setDeletedAt(binEntity.getDeletedAt() != null ? binEntity.getDeletedAt().toString() : null);
            dto.setOriginalPath(binEntity.getOriginalPath());
        }
        return dto;
    }
}
