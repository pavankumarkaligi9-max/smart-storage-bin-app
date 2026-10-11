package com.smartstoragebin.backend.service;

import com.smartstoragebin.backend.dto.*;
import java.time.LocalDate;
import java.util.List;

public interface FileService {
    List<FileDTO> getAllFiles();
    FileDTO getFileById(Long id);
    FileDTO createFile(CreateFileRequest request);
    FileDTO updateFile(Long id, UpdateFileRequest request);
    void moveToBin(Long id);
    FileDTO extendExpiry(Long id, ExtendExpiryRequest request);
    FileDTO convertToPermanent(Long id);
    List<FileDTO> getTemporaryFiles();
    List<FileDTO> getPermanentFiles();
    List<FileDTO> getExpiredFiles();
    List<FileDTO> searchFiles(String query, String category, String status, Long minSize, Long maxSize, LocalDate createdDate, LocalDate expiryDate);
}
