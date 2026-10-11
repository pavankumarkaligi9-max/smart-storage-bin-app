package com.smartstoragebin.backend.scheduler;

import com.smartstoragebin.backend.dto.FileDTO;
import com.smartstoragebin.backend.dto.StorageResponse;
import com.smartstoragebin.backend.entity.enums.NotificationType;
import com.smartstoragebin.backend.service.FileService;
import com.smartstoragebin.backend.service.NotificationService;
import com.smartstoragebin.backend.service.SettingsService;
import com.smartstoragebin.backend.service.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class FileLifecycleScheduler {

    private static final Logger log = LoggerFactory.getLogger(FileLifecycleScheduler.class);

    private final FileService fileService;
    private final NotificationService notificationService;
    private final SettingsService settingsService;
    private final StorageService storageService;

    @Value("${app.storage.warning-threshold-percent:80}")
    private int storageWarningThresholdPercent;

    public FileLifecycleScheduler(FileService fileService, NotificationService notificationService,
                                  SettingsService settingsService, StorageService storageService) {
        this.fileService = fileService;
        this.notificationService = notificationService;
        this.settingsService = settingsService;
        this.storageService = storageService;
    }

    /**
     * Periodic lifecycle scheduler.
     * Checks expiring & expired files, and creates notifications without auto-deleting.
     */
    @Scheduled(cron = "0 0 * * * *") // Runs every hour
    public void runLifecycleChecks() {
        log.info("Running Smart Storage Bin Lifecycle checks...");

        int warningDays = settingsService.getExpiryWarningDays();
        LocalDate today = LocalDate.now();
        LocalDate warningThresholdDate = today.plusDays(warningDays);

        List<FileDTO> tempFiles = fileService.getTemporaryFiles();

        for (FileDTO file : tempFiles) {
            if (file.getExpiryDate() != null && !file.getExpiryDate().trim().isEmpty()) {
                try {
                    LocalDate expiryDate = LocalDate.parse(file.getExpiryDate().substring(0, 10));

                    if (expiryDate.isBefore(today)) {
                        // Expired: notify user, DO NOT auto-delete
                        notificationService.createNotification(
                                "Temporary File Expired",
                                "The temporary storage period for '" + file.getName() + "' has ended. Choose to delete, extend, or keep permanently.",
                                NotificationType.EXPIRED,
                                file.getId()
                        );
                    } else if (!expiryDate.isAfter(warningThresholdDate)) {
                        // Expiring soon: warning notification
                        notificationService.createNotification(
                                "File Expiring Soon",
                                "The file '" + file.getName() + "' will expire on " + expiryDate + ". Review or extend retention.",
                                NotificationType.EXPIRING_SOON,
                                file.getId()
                        );
                    }
                } catch (Exception e) {
                    log.error("Error parsing expiry date for file: {}", file.getId(), e);
                }
            }
        }

        // Check storage usage threshold
        try {
            StorageResponse storage = storageService.getStorageInfo();
            if (storage.getTotalStorage() != null && storage.getTotalStorage() > 0) {
                long usedPercent = (storage.getUsedStorage() * 100) / storage.getTotalStorage();
                if (usedPercent >= storageWarningThresholdPercent) {
                    notificationService.createNotification(
                            "Storage Capacity Warning",
                            "Your storage is " + usedPercent + "% full. Consider reviewing expired temporary files or duplicates.",
                            NotificationType.STORAGE_WARNING,
                            null
                    );
                }
            }
        } catch (Exception e) {
            log.error("Error evaluating storage capacity threshold", e);
        }
    }
}
