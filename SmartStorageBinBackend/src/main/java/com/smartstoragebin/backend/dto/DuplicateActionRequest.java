package com.smartstoragebin.backend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.smartstoragebin.backend.entity.enums.DuplicateAction;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DuplicateActionRequest {
    @NotNull
    private DuplicateAction action;
    
    private Long targetFileId;
    private CreateFileRequest newFile;
}
