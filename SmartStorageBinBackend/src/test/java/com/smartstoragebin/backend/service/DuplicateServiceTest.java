package com.smartstoragebin.backend.service;

import com.smartstoragebin.backend.dto.DuplicateCheckResponse;
import com.smartstoragebin.backend.dto.DuplicateGroupDTO;
import com.smartstoragebin.backend.entity.FileEntity;
import com.smartstoragebin.backend.entity.enums.CategoryType;
import com.smartstoragebin.backend.entity.enums.FileStatus;
import com.smartstoragebin.backend.repository.FileRepository;
import com.smartstoragebin.backend.service.impl.DuplicateServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DuplicateServiceTest {

    @Mock
    private FileRepository fileRepository;

    @Mock
    private FileService fileService;

    @InjectMocks
    private DuplicateServiceImpl duplicateService;

    private FileEntity file1;
    private FileEntity file2;

    @BeforeEach
    void setUp() {
        String sharedHash = "duplicate_hash_123456";
        file1 = FileEntity.builder()
                .id(1L)
                .fileName("Original.pdf")
                .filePath("/storage/Original.pdf")
                .fileSize(2000000L)
                .category(CategoryType.DOCUMENTS)
                .status(FileStatus.PERMANENT)
                .hash(sharedHash)
                .isInBin(false)
                .build();

        file2 = FileEntity.builder()
                .id(2L)
                .fileName("Copy.pdf")
                .filePath("/storage/Copy.pdf")
                .fileSize(2000000L)
                .category(CategoryType.DOCUMENTS)
                .status(FileStatus.PERMANENT)
                .hash(sharedHash)
                .isInBin(false)
                .build();
    }

    @Test
    void testCheckForDuplicates_Found() {
        when(fileRepository.findByHashAndIsInBinFalse("duplicate_hash_123456")).thenReturn(List.of(file1));

        DuplicateCheckResponse response = duplicateService.checkForDuplicates("duplicate_hash_123456");

        assertNotNull(response);
        assertTrue(response.getIsDuplicate());
        assertEquals("duplicate_hash_123456", response.getHash());
        assertEquals(1, response.getExistingFiles().size());
        assertEquals(2000000L, response.getWastedSpace());
    }

    @Test
    void testGetAllDuplicateGroups() {
        when(fileRepository.findByIsInBinFalse()).thenReturn(List.of(file1, file2));

        List<DuplicateGroupDTO> groups = duplicateService.getAllDuplicateGroups();

        assertNotNull(groups);
        assertEquals(1, groups.size());
        assertEquals("duplicate_hash_123456", groups.get(0).getHash());
        assertEquals(2, groups.get(0).getFiles().size());
        assertEquals(2000000L, groups.get(0).getTotalWastedSpace());
    }
}
