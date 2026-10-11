package com.smartstoragebin.backend.service;

import com.smartstoragebin.backend.dto.CategoryResponse;
import com.smartstoragebin.backend.dto.FileDTO;
import com.smartstoragebin.backend.entity.FileEntity;
import com.smartstoragebin.backend.entity.enums.CategoryType;
import com.smartstoragebin.backend.entity.enums.FileStatus;
import com.smartstoragebin.backend.repository.FileRepository;
import com.smartstoragebin.backend.service.impl.CategoryServiceImpl;
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
class CategoryServiceTest {

    @Mock
    private FileRepository fileRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private FileEntity docFile;
    private FileEntity imgFile;

    @BeforeEach
    void setUp() {
        docFile = FileEntity.builder()
                .id(1L)
                .fileName("Document.pdf")
                .filePath("/storage/Document.pdf")
                .fileSize(3000000L)
                .category(CategoryType.DOCUMENTS)
                .status(FileStatus.PERMANENT)
                .isInBin(false)
                .build();

        imgFile = FileEntity.builder()
                .id(2L)
                .fileName("Image.jpg")
                .filePath("/storage/Image.jpg")
                .fileSize(1500000L)
                .category(CategoryType.IMAGES)
                .status(FileStatus.PERMANENT)
                .isInBin(false)
                .build();
    }

    @Test
    void testGetAllCategories() {
        when(fileRepository.findByIsInBinFalse()).thenReturn(List.of(docFile, imgFile));

        List<CategoryResponse> categories = categoryService.getAllCategories();

        assertNotNull(categories);
        assertEquals(CategoryType.values().length, categories.size());

        CategoryResponse docCategory = categories.stream()
                .filter(c -> c.getName().equalsIgnoreCase("Documents"))
                .findFirst()
                .orElse(null);

        assertNotNull(docCategory);
        assertEquals(1, docCategory.getFileCount());
        assertEquals(3000000L, docCategory.getTotalSize());
    }

    @Test
    void testGetFilesByCategory() {
        when(fileRepository.findByCategoryAndIsInBinFalse(CategoryType.DOCUMENTS)).thenReturn(List.of(docFile));

        List<FileDTO> files = categoryService.getFilesByCategory("DOCUMENTS");

        assertNotNull(files);
        assertEquals(1, files.size());
        assertEquals("Document.pdf", files.get(0).getName());
    }
}
