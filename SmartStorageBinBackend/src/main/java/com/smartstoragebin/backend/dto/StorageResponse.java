package com.smartstoragebin.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StorageResponse {
    private Long totalStorage;
    private Long usedStorage;
    private Long freeStorage;
    private Long temporaryStorage;
    private Long duplicateStorage;
    private Long binStorage;
}
