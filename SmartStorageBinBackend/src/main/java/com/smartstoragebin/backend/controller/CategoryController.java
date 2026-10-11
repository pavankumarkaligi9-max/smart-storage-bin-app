package com.smartstoragebin.backend.controller;

import com.smartstoragebin.backend.dto.CategoryResponse;
import com.smartstoragebin.backend.dto.FileDTO;
import com.smartstoragebin.backend.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Tag(name = "Categories", description = "Category management API")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    @Operation(summary = "List categories", description = "Returns all available file categories")
    public List<CategoryResponse> getCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/{category}")
    @Operation(summary = "Get files by category", description = "Returns files matching the specified category")
    public List<FileDTO> getFilesByCategory(@PathVariable String category) {
        return categoryService.getFilesByCategory(category);
    }
}
