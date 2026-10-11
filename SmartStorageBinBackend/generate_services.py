import os

base_dir = r"C:\Users\Pavan Kumar\.gemini\antigravity\scratch\SmartStorageBinBackend\src\main\java\com\smartstoragebin\backend"
service_dir = os.path.join(base_dir, "service")
impl_dir = os.path.join(service_dir, "impl")
scheduler_dir = os.path.join(base_dir, "scheduler")

os.makedirs(impl_dir, exist_ok=True)
os.makedirs(scheduler_dir, exist_ok=True)

files = {
    os.path.join(service_dir, "FileService.java"): """package com.smartstoragebin.backend.service;

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
""",
    os.path.join(impl_dir, "FileServiceImpl.java"): """package com.smartstoragebin.backend.service.impl;

import com.smartstoragebin.backend.service.FileService;
import com.smartstoragebin.backend.service.SettingsService;
import com.smartstoragebin.backend.dto.*;
import com.smartstoragebin.backend.entity.*;
import com.smartstoragebin.backend.repository.*;
import com.smartstoragebin.backend.exception.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;
    private final BinRepository binRepository;
    private final SettingsService settingsService;

    public FileServiceImpl(FileRepository fileRepository, BinRepository binRepository, SettingsService settingsService) {
        this.fileRepository = fileRepository;
        this.binRepository = binRepository;
        this.settingsService = settingsService;
    }

    @Override
    public List<FileDTO> getAllFiles() {
        return fileRepository.findByIsInBinFalse().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public FileDTO getFileById(Long id) {
        return mapToDTO(fileRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("File not found")));
    }

    @Override
    public FileDTO createFile(CreateFileRequest request) {
        FileEntity file = new FileEntity();
        file.setFilename(request.getFilename());
        file.setPath(request.getPath());
        file.setSize(request.getSize());
        file.setHash(request.getHash());
        file.setCategory(request.getCategory() != null ? request.getCategory() : "UNKNOWN");
        file.setFileType(request.getFileType() != null ? request.getFileType() : "application/octet-stream");
        file.setStatus(request.getStatus());
        file.setCreatedDate(LocalDate.now());
        file.setIsInBin(false);
        
        if ("TEMPORARY".equalsIgnoreCase(request.getStatus())) {
            int days = request.getCustomExpiryDays() != null ? request.getCustomExpiryDays() : settingsService.getDefaultExpiryDays();
            file.setExpiryDate(LocalDate.now().plusDays(days));
        }
        return mapToDTO(fileRepository.save(file));
    }

    @Override
    public FileDTO updateFile(Long id, UpdateFileRequest request) {
        FileEntity file = fileRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("File not found"));
        if(request.getFilename() != null) file.setFilename(request.getFilename());
        if(request.getCategory() != null) file.setCategory(request.getCategory());
        return mapToDTO(fileRepository.save(file));
    }

    @Override
    public void moveToBin(Long id) {
        FileEntity file = fileRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("File not found"));
        file.setIsInBin(true);
        BinFileEntity binFile = new BinFileEntity();
        binFile.setFile(file);
        binFile.setOriginalStatus(file.getStatus());
        binFile.setOriginalPath(file.getPath());
        binFile.setDeletedDate(LocalDate.now());
        binRepository.save(binFile);
        fileRepository.save(file);
    }

    @Override
    public FileDTO extendExpiry(Long id, ExtendExpiryRequest request) {
        FileEntity file = fileRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("File not found"));
        if (!"TEMPORARY".equalsIgnoreCase(file.getStatus())) throw new IllegalArgumentException("Not a temporary file");
        file.setExpiryDate(file.getExpiryDate().plusDays(request.getDays()));
        return mapToDTO(fileRepository.save(file));
    }

    @Override
    public FileDTO convertToPermanent(Long id) {
        FileEntity file = fileRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("File not found"));
        file.setStatus("PERMANENT");
        file.setExpiryDate(null);
        return mapToDTO(fileRepository.save(file));
    }

    @Override
    public List<FileDTO> getTemporaryFiles() {
        return fileRepository.findByStatusAndIsInBinFalse("TEMPORARY").stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public List<FileDTO> getPermanentFiles() {
        return fileRepository.findByStatusAndIsInBinFalse("PERMANENT").stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public List<FileDTO> getExpiredFiles() {
        return fileRepository.findByStatusAndExpiryDateBeforeAndIsInBinFalse("TEMPORARY", LocalDate.now()).stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public List<FileDTO> searchFiles(String query, String category, String status, Long minSize, Long maxSize, LocalDate createdDate, LocalDate expiryDate) {
        // Simplified search implementation
        return fileRepository.findAll().stream().filter(f -> !f.getIsInBin()).map(this::mapToDTO).collect(Collectors.toList());
    }

    private FileDTO mapToDTO(FileEntity entity) {
        FileDTO dto = new FileDTO();
        dto.setId(entity.getId());
        dto.setFilename(entity.getFilename());
        dto.setCategory(entity.getCategory());
        dto.setFileType(entity.getFileType());
        dto.setSize(entity.getSize());
        dto.setStatus(entity.getStatus());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setExpiryDate(entity.getExpiryDate());
        dto.setIsInBin(entity.getIsInBin());
        return dto;
    }
}
""",
    os.path.join(service_dir, "DuplicateService.java"): """package com.smartstoragebin.backend.service;

import com.smartstoragebin.backend.dto.*;
import java.util.List;

public interface DuplicateService {
    DuplicateCheckResponse checkForDuplicates(String hash);
    List<DuplicateGroupDTO> getAllDuplicateGroups();
    List<FileDTO> getDuplicatesByHash(String hash);
    FileDTO handleDuplicateAction(DuplicateActionRequest request);
}
""",
    os.path.join(impl_dir, "DuplicateServiceImpl.java"): """package com.smartstoragebin.backend.service.impl;

import com.smartstoragebin.backend.service.DuplicateService;
import com.smartstoragebin.backend.dto.*;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DuplicateServiceImpl implements DuplicateService {
    @Override
    public DuplicateCheckResponse checkForDuplicates(String hash) { return new DuplicateCheckResponse(); }
    @Override
    public List<DuplicateGroupDTO> getAllDuplicateGroups() { return List.of(); }
    @Override
    public List<FileDTO> getDuplicatesByHash(String hash) { return List.of(); }
    @Override
    public FileDTO handleDuplicateAction(DuplicateActionRequest request) { return new FileDTO(); }
}
""",
    os.path.join(service_dir, "CategoryService.java"): """package com.smartstoragebin.backend.service;

import com.smartstoragebin.backend.dto.*;
import java.util.List;

public interface CategoryService {
    List<CategoryResponse> getAllCategories();
    List<FileDTO> getFilesByCategory(String categoryName);
}
""",
    os.path.join(impl_dir, "CategoryServiceImpl.java"): """package com.smartstoragebin.backend.service.impl;

import com.smartstoragebin.backend.service.CategoryService;
import com.smartstoragebin.backend.dto.*;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Override
    public List<CategoryResponse> getAllCategories() { return List.of(); }
    @Override
    public List<FileDTO> getFilesByCategory(String categoryName) { return List.of(); }
}
""",
    os.path.join(service_dir, "StorageService.java"): """package com.smartstoragebin.backend.service;

import com.smartstoragebin.backend.dto.*;

public interface StorageService {
    StorageResponse getStorageInfo();
    TemporaryDashboardResponse getTemporaryDashboard();
}
""",
    os.path.join(impl_dir, "StorageServiceImpl.java"): """package com.smartstoragebin.backend.service.impl;

import com.smartstoragebin.backend.service.StorageService;
import com.smartstoragebin.backend.dto.*;
import org.springframework.stereotype.Service;

@Service
public class StorageServiceImpl implements StorageService {
    @Override
    public StorageResponse getStorageInfo() { return new StorageResponse(); }
    @Override
    public TemporaryDashboardResponse getTemporaryDashboard() { return new TemporaryDashboardResponse(); }
}
""",
    os.path.join(service_dir, "BinService.java"): """package com.smartstoragebin.backend.service;

import com.smartstoragebin.backend.dto.*;
import java.util.List;

public interface BinService {
    List<BinFileDTO> getBinFiles();
    FileDTO restoreFromBin(Long id);
    void deletePermanently(Long id);
    void emptyBin();
}
""",
    os.path.join(impl_dir, "BinServiceImpl.java"): """package com.smartstoragebin.backend.service.impl;

import com.smartstoragebin.backend.service.BinService;
import com.smartstoragebin.backend.dto.*;
import com.smartstoragebin.backend.entity.*;
import com.smartstoragebin.backend.repository.*;
import com.smartstoragebin.backend.exception.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class BinServiceImpl implements BinService {
    private final BinRepository binRepository;
    private final FileRepository fileRepository;
    
    public BinServiceImpl(BinRepository binRepository, FileRepository fileRepository) {
        this.binRepository = binRepository;
        this.fileRepository = fileRepository;
    }

    @Override
    public List<BinFileDTO> getBinFiles() { return List.of(); }

    @Override
    public FileDTO restoreFromBin(Long id) {
        BinFileEntity binFile = binRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Bin item not found"));
        FileEntity file = binFile.getFile();
        file.setIsInBin(false);
        file.setStatus(binFile.getOriginalStatus());
        binRepository.delete(binFile);
        fileRepository.save(file);
        return new FileDTO();
    }

    @Override
    public void deletePermanently(Long id) {
        binRepository.deleteById(id);
    }

    @Override
    public void emptyBin() {
        binRepository.deleteAll();
    }
}
""",
    os.path.join(service_dir, "NotificationService.java"): """package com.smartstoragebin.backend.service;

import com.smartstoragebin.backend.dto.*;
import com.smartstoragebin.backend.enums.NotificationType;
import java.util.List;

public interface NotificationService {
    List<NotificationDTO> getNotifications();
    List<NotificationDTO> getUnreadNotifications();
    void markAsRead(Long id);
    NotificationDTO createNotification(String title, String message, NotificationType type, Long fileId);
}
""",
    os.path.join(impl_dir, "NotificationServiceImpl.java"): """package com.smartstoragebin.backend.service.impl;

import com.smartstoragebin.backend.service.NotificationService;
import com.smartstoragebin.backend.dto.*;
import com.smartstoragebin.backend.enums.NotificationType;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {
    @Override
    public List<NotificationDTO> getNotifications() { return List.of(); }
    @Override
    public List<NotificationDTO> getUnreadNotifications() { return List.of(); }
    @Override
    public void markAsRead(Long id) {}
    @Override
    public NotificationDTO createNotification(String title, String message, NotificationType type, Long fileId) { return new NotificationDTO(); }
}
""",
    os.path.join(service_dir, "SettingsService.java"): """package com.smartstoragebin.backend.service;

import com.smartstoragebin.backend.dto.*;

public interface SettingsService {
    SettingsDTO getSettings();
    SettingsDTO updateSettings(SettingsDTO dto);
    int getDefaultExpiryDays();
    int getExpiryWarningDays();
}
""",
    os.path.join(impl_dir, "SettingsServiceImpl.java"): """package com.smartstoragebin.backend.service.impl;

import com.smartstoragebin.backend.service.SettingsService;
import com.smartstoragebin.backend.dto.*;
import org.springframework.stereotype.Service;

@Service
public class SettingsServiceImpl implements SettingsService {
    @Override
    public SettingsDTO getSettings() { return new SettingsDTO(); }
    @Override
    public SettingsDTO updateSettings(SettingsDTO dto) { return new SettingsDTO(); }
    @Override
    public int getDefaultExpiryDays() { return 30; }
    @Override
    public int getExpiryWarningDays() { return 7; }
}
""",
    os.path.join(scheduler_dir, "FileLifecycleScheduler.java"): """package com.smartstoragebin.backend.scheduler;

import com.smartstoragebin.backend.service.FileService;
import com.smartstoragebin.backend.service.NotificationService;
import com.smartstoragebin.backend.service.SettingsService;
import com.smartstoragebin.backend.service.StorageService;
import com.smartstoragebin.backend.dto.FileDTO;
import com.smartstoragebin.backend.enums.NotificationType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class FileLifecycleScheduler {

    private final FileService fileService;
    private final NotificationService notificationService;
    private final SettingsService settingsService;
    private final StorageService storageService;

    public FileLifecycleScheduler(FileService fileService, NotificationService notificationService,
                                  SettingsService settingsService, StorageService storageService) {
        this.fileService = fileService;
        this.notificationService = notificationService;
        this.settingsService = settingsService;
        this.storageService = storageService;
    }

    @Scheduled(cron = "0 0 * * * *") // Runs hourly
    public void checkExpiringAndExpiredFiles() {
        int warningDays = settingsService.getExpiryWarningDays();
        LocalDate now = LocalDate.now();
        LocalDate warningDate = now.plusDays(warningDays);

        List<FileDTO> tempFiles = fileService.getTemporaryFiles();

        for (FileDTO file : tempFiles) {
            if (file.getExpiryDate() != null) {
                if (file.getExpiryDate().isBefore(now)) {
                    notificationService.createNotification(
                            "File Expired",
                            "File " + file.getFilename() + " has expired.",
                            NotificationType.EXPIRED,
                            file.getId()
                    );
                } else if (file.getExpiryDate().isBefore(warningDate) || file.getExpiryDate().isEqual(warningDate)) {
                    notificationService.createNotification(
                            "File Expiring Soon",
                            "File " + file.getFilename() + " is expiring in less than " + warningDays + " days.",
                            NotificationType.EXPIRING_SOON,
                            file.getId()
                    );
                }
            }
        }
        
        // Note: Storage threshold logic and recycle bin auto-clean logic 
        // can be implemented similarly when thresholds and bin items are retrievable.
    }
}
"""
}

for path, content in files.items():
    with open(path, "w", encoding="utf-8") as f:
        f.write(content)

print("Created all files.")
