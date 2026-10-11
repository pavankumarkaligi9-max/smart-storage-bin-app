package com.smartstoragebin.backend.repository;

import com.smartstoragebin.backend.entity.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {

    List<NotificationEntity> findAllByOrderByCreatedAtDesc();

    List<NotificationEntity> findByIsReadFalseOrderByCreatedAtDesc();
}
