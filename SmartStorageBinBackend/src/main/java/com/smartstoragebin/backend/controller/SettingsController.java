package com.smartstoragebin.backend.controller;

import com.smartstoragebin.backend.dto.SettingsDTO;
import com.smartstoragebin.backend.service.SettingsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/settings")
@RequiredArgsConstructor
@Tag(name = "Settings", description = "Application settings API")
public class SettingsController {

    private final SettingsService settingsService;

    @GetMapping
    @Operation(summary = "Get settings", description = "Retrieves user and application settings")
    public SettingsDTO getSettings() {
        return settingsService.getSettings();
    }

    @PutMapping
    @Operation(summary = "Update settings", description = "Updates application settings")
    public SettingsDTO updateSettings(@RequestBody SettingsDTO settingsDTO) {
        return settingsService.updateSettings(settingsDTO);
    }
}
