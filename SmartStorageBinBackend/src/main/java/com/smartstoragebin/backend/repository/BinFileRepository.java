package com.smartstoragebin.backend.repository;

import com.smartstoragebin.backend.entity.BinFileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BinFileRepository extends JpaRepository<BinFileEntity, Long> {

    List<BinFileEntity> findAllByOrderByDeletedAtDesc();

    Optional<BinFileEntity> findByFileId(Long fileId);

    void deleteByFileId(Long fileId);
}
