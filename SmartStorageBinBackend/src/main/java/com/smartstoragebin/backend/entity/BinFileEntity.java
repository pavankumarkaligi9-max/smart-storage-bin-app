package com.smartstoragebin.backend.entity;

import com.smartstoragebin.backend.entity.enums.FileStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "bin_files")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BinFileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_id", nullable = false, unique = true)
    private FileEntity file;

    @Column(nullable = false)
    private String originalPath;

    @Column(nullable = false)
    private LocalDateTime deletedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FileStatus originalStatus;

    private LocalDate originalExpiryDate;
}
