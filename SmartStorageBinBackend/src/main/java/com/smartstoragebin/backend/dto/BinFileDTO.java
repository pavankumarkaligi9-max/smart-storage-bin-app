package com.smartstoragebin.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BinFileDTO {
    private Long id;
    private Long fileId;
    private String name;
    private String originalPath;
    private Long size;
    private String type;
    private String originalStatus;
    private String deletedAt;
    private String originalExpiryDate;
}
