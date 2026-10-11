package com.smartstoragebin.backend.service.impl;

import com.smartstoragebin.backend.dto.NotificationDTO;
import com.smartstoragebin.backend.entity.NotificationEntity;
import com.smartstoragebin.backend.entity.enums.NotificationType;
import com.smartstoragebin.backend.exception.ResourceNotFoundException;
import com.smartstoragebin.backend.repository.NotificationRepository;
import com.smartstoragebin.backend.service.NotificationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationServiceImpl(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationDTO> getNotifications() {
        return notificationRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationDTO> getUnreadNotifications() {
        return notificationRepository.findByIsReadFalseOrderByCreatedAtDesc().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void markAsRead(Long id) {
        NotificationEntity notification = notificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found with id: " + id));
        notification.setIsRead(true);
        notificationRepository.save(notification);
    }

    @Override
    public NotificationDTO createNotification(String title, String message, NotificationType type, Long fileId) {
        NotificationEntity notification = NotificationEntity.builder()
                .title(title)
                .message(message)
                .type(type)
                .fileId(fileId)
                .isRead(false)
                .build();

        NotificationEntity saved = notificationRepository.save(notification);
        return mapToDTO(saved);
    }

    private NotificationDTO mapToDTO(NotificationEntity entity) {
        if (entity == null) return null;
        return NotificationDTO.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .message(entity.getMessage())
                .type(entity.getType() != null ? entity.getType().name() : null)
                .fileId(entity.getFileId())
                .createdAt(entity.getCreatedAt() != null ? entity.getCreatedAt().toString() : null)
                .isRead(entity.getIsRead() != null ? entity.getIsRead() : false)
                .build();
    }
}
