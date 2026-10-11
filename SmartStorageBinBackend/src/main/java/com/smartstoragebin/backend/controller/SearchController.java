package com.smartstoragebin.backend.controller;

import com.smartstoragebin.backend.dto.FileDTO;
import com.smartstoragebin.backend.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
@Tag(name = "Search", description = "Global multi-filter search APIs")
public class SearchController {

    private final FileService fileService;

    @GetMapping
    @Operation(summary = "Search files with multi-criteria filters", description = "Search files by query, category, status (Temporary/Permanent/Expired/Expiring Soon), size range, and dates")
    public List<FileDTO> searchFiles(
            @Parameter(description = "Search keyword in filename or extension") @RequestParam(required = false) String query,
            @Parameter(description = "Category filter: DOCUMENTS, IMAGES, VIDEOS, AUDIO, ARCHIVES, APPLICATIONS, OTHER") @RequestParam(required = false) String category,
            @Parameter(description = "Status filter: TEMPORARY, PERMANENT, EXPIRED, EXPIRING_SOON") @RequestParam(required = false) String status,
            @Parameter(description = "Minimum size in bytes") @RequestParam(required = false) Long minimumSize,
            @Parameter(description = "Maximum size in bytes") @RequestParam(required = false) Long maximumSize,
            @Parameter(description = "Creation date (YYYY-MM-DD)") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate createdDate,
            @Parameter(description = "Expiry date (YYYY-MM-DD)") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate expiryDate) {
        
        return fileService.searchFiles(query, category, status, minimumSize, maximumSize, createdDate, expiryDate);
    }
}
