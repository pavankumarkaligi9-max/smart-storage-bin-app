package com.smartstoragebin.backend.config;

import com.smartstoragebin.backend.entity.BinFileEntity;
import com.smartstoragebin.backend.entity.FileEntity;
import com.smartstoragebin.backend.entity.NotificationEntity;
import com.smartstoragebin.backend.entity.SettingsEntity;
import com.smartstoragebin.backend.entity.enums.CategoryType;
import com.smartstoragebin.backend.entity.enums.FileStatus;
import com.smartstoragebin.backend.entity.enums.NotificationType;
import com.smartstoragebin.backend.repository.BinFileRepository;
import com.smartstoragebin.backend.repository.FileRepository;
import com.smartstoragebin.backend.repository.NotificationRepository;
import com.smartstoragebin.backend.repository.SettingsRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Configuration
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final SettingsRepository settingsRepository;
    private final FileRepository fileRepository;
    private final BinFileRepository binFileRepository;
    private final NotificationRepository notificationRepository;

    @Override
    public void run(String... args) {
        log.info("Checking and initializing default data for Smart Storage Bin...");

        // 1. Initialize Settings
        if (settingsRepository.count() == 0) {
            settingsRepository.save(SettingsEntity.builder()
                    .notificationEnabled(true)
                    .defaultExpiryDays(7)
                    .expiryWarningDays(3)
                    .binRetentionDays(30)
                    .duplicateDetectionEnabled(true)
                    .build());
            log.info("Default settings initialized.");
        }

        // 2. Initialize Sample Files if database is empty
        if (fileRepository.count() == 0) {
            FileEntity f1 = fileRepository.save(FileEntity.builder()
                    .fileName("DBMS Notes.pdf")
                    .filePath("/storage/emulated/0/Download/DBMS Notes.pdf")
                    .fileSize(4500000L)
                    .fileType("PDF")
                    .category(CategoryType.DOCUMENTS)
                    .status(FileStatus.TEMPORARY)
                    .hash("e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855")
                    .expiryDate(LocalDate.now().plusDays(3))
                    .isInBin(false)
                    .lastAccessed(LocalDateTime.now())
                    .build());

            FileEntity f2 = fileRepository.save(FileEntity.builder()
                    .fileName("OS Lecture.mp4")
                    .filePath("/storage/emulated/0/Movies/OS Lecture.mp4")
                    .fileSize(512000000L)
                    .fileType("MP4")
                    .category(CategoryType.VIDEOS)
                    .status(FileStatus.PERMANENT)
                    .hash("a1b2c3d4e5f67890123456789abcdef0123456789abcdef0123456789abcdef0")
                    .expiryDate(null)
                    .isInBin(false)
                    .lastAccessed(LocalDateTime.now())
                    .build());

            FileEntity f3 = fileRepository.save(FileEntity.builder()
                    .fileName("Vacation Photo.jpg")
                    .filePath("/storage/emulated/0/DCIM/Camera/Vacation Photo.jpg")
                    .fileSize(3200000L)
                    .fileType("JPG")
                    .category(CategoryType.IMAGES)
                    .status(FileStatus.PERMANENT)
                    .hash("b2c3d4e5f6a17890123456789abcdef0123456789abcdef0123456789abcdef1")
                    .expiryDate(null)
                    .isInBin(false)
                    .lastAccessed(LocalDateTime.now())
                    .build());

            FileEntity f4 = fileRepository.save(FileEntity.builder()
                    .fileName("Assignment 1.docx")
                    .filePath("/storage/emulated/0/Documents/Assignment 1.docx")
                    .fileSize(1200000L)
                    .fileType("DOCX")
                    .category(CategoryType.DOCUMENTS)
                    .status(FileStatus.TEMPORARY)
                    .hash("c3d4e5f6a1b27890123456789abcdef0123456789abcdef0123456789abcdef2")
                    .expiryDate(LocalDate.now().plusDays(1))
                    .isInBin(false)
                    .lastAccessed(LocalDateTime.now())
                    .build());

            FileEntity f5 = fileRepository.save(FileEntity.builder()
                    .fileName("Old Notes.pdf")
                    .filePath("/storage/emulated/0/Download/Old Notes.pdf")
                    .fileSize(5400000L)
                    .fileType("PDF")
                    .category(CategoryType.DOCUMENTS)
                    .status(FileStatus.TEMPORARY)
                    .hash("d4e5f6a1b2c37890123456789abcdef0123456789abcdef0123456789abcdef3")
                    .expiryDate(LocalDate.now().minusDays(2))
                    .isInBin(false)
                    .lastAccessed(LocalDateTime.now())
                    .build());

            FileEntity f6 = fileRepository.save(FileEntity.builder()
                    .fileName("Screenshot_1.png")
                    .filePath("/storage/emulated/0/Pictures/Screenshots/Screenshot_1.png")
                    .fileSize(2500000L)
                    .fileType("PNG")
                    .category(CategoryType.IMAGES)
                    .status(FileStatus.TEMPORARY)
                    .hash("e5f6a1b2c3d47890123456789abcdef0123456789abcdef0123456789abcdef4")
                    .expiryDate(LocalDate.now().plusDays(2))
                    .isInBin(false)
                    .lastAccessed(LocalDateTime.now())
                    .build());

            FileEntity f7 = fileRepository.save(FileEntity.builder()
                    .fileName("Music Track.mp3")
                    .filePath("/storage/emulated/0/Music/Music Track.mp3")
                    .fileSize(6500000L)
                    .fileType("MP3")
                    .category(CategoryType.AUDIO)
                    .status(FileStatus.PERMANENT)
                    .hash("f6a1b2c3d4e57890123456789abcdef0123456789abcdef0123456789abcdef5")
                    .expiryDate(null)
                    .isInBin(false)
                    .lastAccessed(LocalDateTime.now())
                    .build());

            FileEntity f8 = fileRepository.save(FileEntity.builder()
                    .fileName("Project Archive.zip")
                    .filePath("/storage/emulated/0/Download/Project Archive.zip")
                    .fileSize(24000000L)
                    .fileType("ZIP")
                    .category(CategoryType.ARCHIVES)
                    .status(FileStatus.PERMANENT)
                    .hash("a7b2c3d4e5f67890123456789abcdef0123456789abcdef0123456789abcdef6")
                    .expiryDate(null)
                    .isInBin(false)
                    .lastAccessed(LocalDateTime.now())
                    .build());

            FileEntity f9 = fileRepository.save(FileEntity.builder()
                    .fileName("App APK.apk")
                    .filePath("/storage/emulated/0/Download/App APK.apk")
                    .fileSize(35000000L)
                    .fileType("APK")
                    .category(CategoryType.APPLICATIONS)
                    .status(FileStatus.PERMANENT)
                    .hash("b8c3d4e5f6a17890123456789abcdef0123456789abcdef0123456789abcdef7")
                    .expiryDate(null)
                    .isInBin(false)
                    .lastAccessed(LocalDateTime.now())
                    .build());

            FileEntity f10 = fileRepository.save(FileEntity.builder()
                    .fileName("Deleted Image.jpg")
                    .filePath("/storage/emulated/0/DCIM/Camera/Deleted Image.jpg")
                    .fileSize(4000000L)
                    .fileType("JPG")
                    .category(CategoryType.IMAGES)
                    .status(FileStatus.PERMANENT)
                    .hash("c9d4e5f6a1b27890123456789abcdef0123456789abcdef0123456789abcdef8")
                    .expiryDate(null)
                    .isInBin(true)
                    .lastAccessed(LocalDateTime.now())
                    .build());

            // Save Bin entry for deleted file
            binFileRepository.save(BinFileEntity.builder()
                    .file(f10)
                    .originalPath(f10.getFilePath())
                    .originalStatus(FileStatus.PERMANENT)
                    .originalExpiryDate(null)
                    .deletedAt(LocalDateTime.now().minusDays(1))
                    .build());

            // Duplicate files
            fileRepository.save(FileEntity.builder()
                    .fileName("Duplicate Video 1.mp4")
                    .filePath("/storage/emulated/0/Movies/Dup 1.mp4")
                    .fileSize(150000000L)
                    .fileType("MP4")
                    .category(CategoryType.VIDEOS)
                    .status(FileStatus.PERMANENT)
                    .hash("hash_dup_video_123456789abcdef0123456789abcdef0123456789abcdef00")
                    .expiryDate(null)
                    .isInBin(false)
                    .lastAccessed(LocalDateTime.now())
                    .build());

            fileRepository.save(FileEntity.builder()
                    .fileName("Duplicate Video 2.mp4")
                    .filePath("/storage/emulated/0/Download/Dup 2.mp4")
                    .fileSize(150000000L)
                    .fileType("MP4")
                    .category(CategoryType.VIDEOS)
                    .status(FileStatus.PERMANENT)
                    .hash("hash_dup_video_123456789abcdef0123456789abcdef0123456789abcdef00")
                    .expiryDate(null)
                    .isInBin(false)
                    .lastAccessed(LocalDateTime.now())
                    .build());

            // Notifications
            notificationRepository.save(NotificationEntity.builder()
                    .title("Temporary File Expired")
                    .message("The temporary storage period for 'Old Notes.pdf' has ended.")
                    .type(NotificationType.EXPIRED)
                    .fileId(f5.getId())
                    .isRead(false)
                    .build());

            notificationRepository.save(NotificationEntity.builder()
                    .title("File Expiring Soon")
                    .message("DBMS Notes.pdf will expire in 3 days.")
                    .type(NotificationType.EXPIRING_SOON)
                    .fileId(f1.getId())
                    .isRead(false)
                    .build());

            log.info("Sample file records seeded successfully.");
        }
    }
}
