package com.smartstoragebin.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.smartstoragebin.backend.entity.enums.FileStatus;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateFileRequest {
    @NotBlank
    private String name;

    @NotBlank
    private String path;

    @NotNull
    @PositiveOrZero
    private Long size;

    private String type;

    private String category;

    @NotNull
    private FileStatus status;

    private String hash;

    private Integer customExpiryDays;

    private LocalDate expiryDate;
}
