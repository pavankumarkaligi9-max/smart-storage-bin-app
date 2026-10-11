package com.smartstoragebin.backend.controller;

import com.smartstoragebin.backend.dto.DuplicateActionRequest;
import com.smartstoragebin.backend.dto.DuplicateCheckRequest;
import com.smartstoragebin.backend.dto.DuplicateCheckResponse;
import com.smartstoragebin.backend.dto.DuplicateGroupDTO;
import com.smartstoragebin.backend.dto.FileDTO;
import com.smartstoragebin.backend.service.DuplicateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/duplicates")
@RequiredArgsConstructor
@Tag(name = "Duplicates", description = "SHA-256 duplicate detection and resolution APIs")
public class DuplicateController {

    private final DuplicateService duplicateService;

    @GetMapping
    @Operation(summary = "Get all duplicate file groups", description = "Returns clusters of duplicate files sharing the same SHA-256 hash along with wasted space calculations")
    public List<DuplicateGroupDTO> getDuplicates() {
        return duplicateService.getAllDuplicateGroups();
    }

    @GetMapping("/{hash}")
    @Operation(summary = "Get duplicate files by hash", description = "Returns all files matching a specific SHA-256 hash")
    public List<FileDTO> getDuplicatesByHash(@PathVariable String hash) {
        return duplicateService.getDuplicatesByHash(hash);
    }

    @PostMapping("/check")
    @Operation(summary = "Check for duplicate file", description = "Checks if a given SHA-256 hash already exists in storage")
    public DuplicateCheckResponse checkDuplicate(@RequestBody DuplicateCheckRequest request) {
        return duplicateService.checkForDuplicates(request.getHash());
    }

    @PostMapping("/action")
    @Operation(summary = "Execute duplicate resolution action", description = "Handles user decision: KEEP_EXISTING, REPLACE, KEEP_BOTH, or CANCEL")
    public FileDTO duplicateAction(@RequestBody DuplicateActionRequest request) {
        return duplicateService.handleDuplicateAction(request);
    }
}
