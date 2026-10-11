package com.smartstoragebin.backend.service;

import com.smartstoragebin.backend.dto.*;
import java.util.List;

public interface CategoryService {
    List<CategoryResponse> getAllCategories();
    List<FileDTO> getFilesByCategory(String categoryName);
}
