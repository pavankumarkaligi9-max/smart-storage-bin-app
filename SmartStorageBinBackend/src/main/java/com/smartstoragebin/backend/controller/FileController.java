package com.smartstoragebin.backend.controller;

import com.smartstoragebin.backend.dto.CreateFileRequest;
import com.smartstoragebin.backend.dto.ExtendExpiryRequest;
import com.smartstoragebin.backend.dto.FileDTO;
import com.smartstoragebin.backend.dto.TemporaryDashboardResponse;
import com.smartstoragebin.backend.dto.UpdateFileRequest;
import com.smartstoragebin.backend.service.FileService;
import com.smartstoragebin.backend.service.StorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
@Tag(name = "Files", description = "File metadata, lifecycle, and operations management")
public class FileController {

    private final FileService fileService;
    private final StorageService storageService;

    @GetMapping
    @Operation(summary = "List all active files", description = "Retrieves a list of all active files not currently in the recycle bin")
    public List<FileDTO> getAllFiles() {
        return fileService.getAllFiles();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get file details by ID", description = "Retrieves metadata and lifecycle details for a specific file")
    @ApiResponse(responseCode = "200", description = "File retrieved successfully")
    @ApiResponse(responseCode = "404", description = "File not found")
    public FileDTO getFileById(@Parameter(description = "ID of the file to retrieve") @PathVariable Long id) {
        return fileService.getFileById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Register or upload new file", description = "Registers file metadata, calculates initial expiry if temporary, and hashes SHA-256")
    @ApiResponse(responseCode = "201", description = "File registered successfully")
    @ApiResponse(responseCode = "400", description = "Validation error in request")
    public FileDTO createFile(@Valid @RequestBody CreateFileRequest request) {
        return fileService.createFile(request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update file metadata", description = "Updates file name or storage path")
    @ApiResponse(responseCode = "200", description = "File updated successfully")
    @ApiResponse(responseCode = "404", description = "File not found")
    public FileDTO updateFile(@PathVariable Long id, @RequestBody UpdateFileRequest request) {
        return fileService.updateFile(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Move file to recycle bin", description = "Moves file to bin while retaining original status and path for potential restoration")
    @ApiResponse(responseCode = "204", description = "File successfully moved to recycle bin")
    public ResponseEntity<Void> deleteFile(@PathVariable Long id) {
        fileService.moveToBin(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/temporary")
    @Operation(summary = "Get all temporary files", description = "Lists active temporary files with remaining expiry countdowns")
    public List<FileDTO> getTemporaryFiles() {
        return fileService.getTemporaryFiles();
    }

    @GetMapping("/permanent")
    @Operation(summary = "Get all permanent files", description = "Lists permanent files that are never automatically expired")
    public List<FileDTO> getPermanentFiles() {
        return fileService.getPermanentFiles();
    }

    @GetMapping("/expired")
    @Operation(summary = "Get expired temporary files", description = "Lists files whose temporary storage period has ended awaiting user action")
    public List<FileDTO> getExpiredFiles() {
        return fileService.getExpiredFiles();
    }

    @PutMapping("/{id}/extend")
    @Operation(summary = "Extend temporary file expiry", description = "Extends the retention period of a temporary file by specified additional days or new date")
    public FileDTO extendExpiry(@PathVariable Long id, @RequestBody ExtendExpiryRequest request) {
        return fileService.extendExpiry(id, request);
    }

    @PutMapping("/{id}/convert-permanent")
    @Operation(summary = "Convert temporary file to permanent", description = "Stops expiry countdown and preserves file permanently")
    public FileDTO convertToPermanent(@PathVariable Long id) {
        return fileService.convertToPermanent(id);
    }

    @GetMapping("/temporary/summary")
    @Operation(summary = "Get temporary files dashboard summary", description = "Summary statistics for temporary storage management screen")
    public TemporaryDashboardResponse getTemporarySummary() {
        return storageService.getTemporaryDashboard();
    }
}
