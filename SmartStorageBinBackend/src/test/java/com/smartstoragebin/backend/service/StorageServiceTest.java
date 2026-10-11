package com.smartstoragebin.backend.service;

import com.smartstoragebin.backend.dto.StorageResponse;
import com.smartstoragebin.backend.dto.TemporaryDashboardResponse;
import com.smartstoragebin.backend.entity.FileEntity;
import com.smartstoragebin.backend.entity.enums.CategoryType;
import com.smartstoragebin.backend.entity.enums.FileStatus;
import com.smartstoragebin.backend.repository.BinFileRepository;
import com.smartstoragebin.backend.repository.FileRepository;
import com.smartstoragebin.backend.service.impl.StorageServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StorageServiceTest {

    @Mock
    private FileRepository fileRepository;

    @Mock
    private BinFileRepository binFileRepository;

    @Mock
    private SettingsService settingsService;

    @InjectMocks
    private StorageServiceImpl storageService;

    private FileEntity tempFile;
    private FileEntity permFile;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(storageService, "totalStorage", 100000000L);

        tempFile = FileEntity.builder()
                .id(1L)
                .fileName("TempDoc.pdf")
                .filePath("/storage/TempDoc.pdf")
                .fileSize(10000000L)
                .category(CategoryType.DOCUMENTS)
                .status(FileStatus.TEMPORARY)
                .hash("hash1")
                .expiryDate(LocalDate.now().plusDays(2))
                .isInBin(false)
                .build();

        permFile = FileEntity.builder()
                .id(2L)
                .fileName("PermPhoto.jpg")
                .filePath("/storage/PermPhoto.jpg")
                .fileSize(20000000L)
                .category(CategoryType.IMAGES)
                .status(FileStatus.PERMANENT)
                .hash("hash2")
                .isInBin(false)
                .build();
    }

    @Test
    void testGetStorageInfo() {
        when(fileRepository.findByIsInBinFalse()).thenReturn(List.of(tempFile, permFile));
        when(binFileRepository.findAll()).thenReturn(List.of());

        StorageResponse response = storageService.getStorageInfo();

        assertNotNull(response);
        assertEquals(100000000L, response.getTotalStorage());
        assertEquals(30000000L, response.getUsedStorage());
        assertEquals(70000000L, response.getFreeStorage());
        assertEquals(10000000L, response.getTemporaryStorage());
    }

    @Test
    void testGetTemporaryDashboard() {
        when(fileRepository.findByStatusAndIsInBinFalse(FileStatus.TEMPORARY)).thenReturn(List.of(tempFile));
        when(settingsService.getExpiryWarningDays()).thenReturn(3);

        TemporaryDashboardResponse dashboard = storageService.getTemporaryDashboard();

        assertNotNull(dashboard);
        assertEquals(1, dashboard.getTotalTemporaryFiles());
        assertEquals(10000000L, dashboard.getTotalTemporaryStorage());
        assertEquals(1, dashboard.getExpiringSoonCount());
        assertEquals(0, dashboard.getExpiredCount());
    }
}
