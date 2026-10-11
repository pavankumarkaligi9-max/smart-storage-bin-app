package com.smartstoragebin.backend.service;

import com.smartstoragebin.backend.dto.CreateFileRequest;
import com.smartstoragebin.backend.dto.FileDTO;
import com.smartstoragebin.backend.entity.FileEntity;
import com.smartstoragebin.backend.entity.enums.CategoryType;
import com.smartstoragebin.backend.entity.enums.FileStatus;
import com.smartstoragebin.backend.repository.BinFileRepository;
import com.smartstoragebin.backend.repository.FileRepository;
import com.smartstoragebin.backend.service.impl.FileServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FileServiceTest {

    @Mock
    private FileRepository fileRepository;

    @Mock
    private BinFileRepository binFileRepository;

    @Mock
    private SettingsService settingsService;

    @InjectMocks
    private FileServiceImpl fileService;

    private FileEntity sampleFile;

    @BeforeEach
    void setUp() {
        sampleFile = FileEntity.builder()
                .id(1L)
                .fileName("DBMS Notes.pdf")
                .filePath("/storage/Download/DBMS Notes.pdf")
                .fileSize(4500000L)
                .fileType("PDF")
                .category(CategoryType.DOCUMENTS)
                .status(FileStatus.TEMPORARY)
                .hash("hash123")
                .expiryDate(LocalDate.now().plusDays(5))
                .createdAt(LocalDateTime.now())
                .isInBin(false)
                .build();
    }

    @Test
    void testGetAllFiles() {
        when(fileRepository.findByIsInBinFalse()).thenReturn(List.of(sampleFile));

        List<FileDTO> files = fileService.getAllFiles();

        assertNotNull(files);
        assertEquals(1, files.size());
        assertEquals("DBMS Notes.pdf", files.get(0).getName());
        assertEquals("DOCUMENTS", files.get(0).getCategory());
        verify(fileRepository, times(1)).findByIsInBinFalse();
    }

    @Test
    void testGetFileById_Success() {
        when(fileRepository.findByIdAndIsInBinFalse(1L)).thenReturn(Optional.of(sampleFile));
        when(fileRepository.save(any(FileEntity.class))).thenReturn(sampleFile);

        FileDTO result = fileService.getFileById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("DBMS Notes.pdf", result.getName());
    }

    @Test
    void testCreateTemporaryFile_CalculatesExpiry() {
        when(settingsService.getDefaultExpiryDays()).thenReturn(7);
        when(fileRepository.save(any(FileEntity.class))).thenAnswer(invocation -> {
            FileEntity entity = invocation.getArgument(0);
            entity.setId(2L);
            return entity;
        });

        CreateFileRequest request = CreateFileRequest.builder()
                .name("Lecture.mp4")
                .path("/storage/Movies/Lecture.mp4")
                .size(1000000L)
                .status(FileStatus.TEMPORARY)
                .build();

        FileDTO created = fileService.createFile(request);

        assertNotNull(created);
        assertEquals("Lecture.mp4", created.getName());
        assertEquals("VIDEOS", created.getCategory());
        assertNotNull(created.getExpiryDate());
    }

    @Test
    void testConvertToPermanent() {
        when(fileRepository.findByIdAndIsInBinFalse(1L)).thenReturn(Optional.of(sampleFile));
        when(fileRepository.save(any(FileEntity.class))).thenReturn(sampleFile);

        FileDTO converted = fileService.convertToPermanent(1L);

        assertNotNull(converted);
        assertEquals(FileStatus.PERMANENT.name(), sampleFile.getStatus().name());
        assertNull(sampleFile.getExpiryDate());
    }

    @Test
    void testMoveToBin() {
        when(fileRepository.findByIdAndIsInBinFalse(1L)).thenReturn(Optional.of(sampleFile));

        fileService.moveToBin(1L);

        assertTrue(sampleFile.getIsInBin());
        verify(binFileRepository, times(1)).save(any());
        verify(fileRepository, times(1)).save(sampleFile);
    }
}
