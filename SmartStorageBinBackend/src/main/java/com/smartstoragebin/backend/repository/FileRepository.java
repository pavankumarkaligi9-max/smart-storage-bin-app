package com.smartstoragebin.backend.repository;

import com.smartstoragebin.backend.entity.FileEntity;
import com.smartstoragebin.backend.entity.enums.CategoryType;
import com.smartstoragebin.backend.entity.enums.FileStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<FileEntity, Long> {

    List<FileEntity> findByIsInBinFalse();

    Optional<FileEntity> findByIdAndIsInBinFalse(Long id);

    List<FileEntity> findByStatusAndIsInBinFalse(FileStatus status);

    List<FileEntity> findByExpiryDateLessThanEqualAndStatusAndIsInBinFalse(LocalDate date, FileStatus status);

    List<FileEntity> findByExpiryDateBetweenAndStatusAndIsInBinFalse(LocalDate start, LocalDate end, FileStatus status);

    List<FileEntity> findByHashAndIsInBinFalse(String hash);

    List<FileEntity> findByCategoryAndIsInBinFalse(CategoryType category);
    
    @Query("SELECT SUM(f.fileSize) FROM FileEntity f WHERE f.isInBin = false")
    Long sumFileSize();

    @Query("SELECT SUM(f.fileSize) FROM FileEntity f WHERE f.category = :category AND f.isInBin = false")
    Long sumFileSizeByCategory(@Param("category") CategoryType category);
    
    @Query("SELECT SUM(f.fileSize) FROM FileEntity f WHERE f.status = :status AND f.isInBin = false")
    Long sumFileSizeByStatus(@Param("status") FileStatus status);
}
