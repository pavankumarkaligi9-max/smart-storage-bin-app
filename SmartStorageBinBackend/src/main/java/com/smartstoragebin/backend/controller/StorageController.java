package com.smartstoragebin.backend.controller;

import com.smartstoragebin.backend.dto.StorageResponse;
import com.smartstoragebin.backend.dto.TemporaryDashboardResponse;
import com.smartstoragebin.backend.service.StorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Storage", description = "Storage and Dashboard APIs")
public class StorageController {

    private final StorageService storageService;

    @GetMapping("/storage")
    @Operation(summary = "Get storage info", description = "Returns overall storage statistics")
    public StorageResponse getStorage() {
        return storageService.getStorageInfo();
    }

    @GetMapping("/dashboard/temporary")
    @Operation(summary = "Get temporary dashboard", description = "Returns dashboard metrics for temporary storage")
    public TemporaryDashboardResponse getTemporaryDashboard() {
        return storageService.getTemporaryDashboard();
    }
}
