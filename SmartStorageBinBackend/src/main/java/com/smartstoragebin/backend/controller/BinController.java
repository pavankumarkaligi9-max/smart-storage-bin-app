package com.smartstoragebin.backend.controller;

import com.smartstoragebin.backend.dto.BinFileDTO;
import com.smartstoragebin.backend.dto.FileDTO;
import com.smartstoragebin.backend.service.BinService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bin")
@RequiredArgsConstructor
@Tag(name = "Bin", description = "Recycle bin management API")
public class BinController {

    private final BinService binService;

    @GetMapping
    @Operation(summary = "List bin files", description = "Returns all files currently in the recycle bin")
    public List<BinFileDTO> getBinFiles() {
        return binService.getBinFiles();
    }

    @PostMapping("/{id}/restore")
    @Operation(summary = "Restore file", description = "Restores a file from the recycle bin preserving original lifecycle status")
    public FileDTO restoreFile(@PathVariable Long id) {
        return binService.restoreFromBin(id);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete permanently", description = "Permanently deletes a file from the bin")
    public ResponseEntity<Void> deleteFile(@PathVariable Long id) {
        binService.deletePermanently(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/empty")
    @Operation(summary = "Empty bin", description = "Permanently deletes all files in the bin")
    public ResponseEntity<Void> emptyBin() {
        binService.emptyBin();
        return ResponseEntity.noContent().build();
    }
}
