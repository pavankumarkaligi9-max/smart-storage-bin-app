package com.smartstoragebin.backend.service;

import com.smartstoragebin.backend.dto.*;
import com.smartstoragebin.backend.entity.enums.NotificationType;
import java.util.List;

public interface NotificationService {
    List<NotificationDTO> getNotifications();
    List<NotificationDTO> getUnreadNotifications();
    void markAsRead(Long id);
    NotificationDTO createNotification(String title, String message, NotificationType type, Long fileId);
}
