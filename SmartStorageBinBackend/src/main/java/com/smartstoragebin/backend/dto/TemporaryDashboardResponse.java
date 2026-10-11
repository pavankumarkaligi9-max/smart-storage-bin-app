package com.smartstoragebin.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TemporaryDashboardResponse {
    private Integer totalTemporaryFiles;
    private Long totalTemporaryStorage;
    private Integer expiredCount;
    private Integer expiringTodayCount;
    private Integer expiringSoonCount;
    private List<FileDTO> largestTemporaryFiles;
}
