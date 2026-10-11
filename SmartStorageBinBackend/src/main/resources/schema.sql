-- Smart Storage Bin Database Schema Initialization
CREATE DATABASE IF NOT EXISTS smart_file_manager;
USE smart_file_manager;

-- 1. SETTINGS Table
CREATE TABLE IF NOT EXISTS settings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    notification_enabled BOOLEAN NOT NULL DEFAULT TRUE,
    default_expiry_days INT NOT NULL DEFAULT 7,
    expiry_warning_days INT NOT NULL DEFAULT 3,
    bin_retention_days INT NOT NULL DEFAULT 30,
    duplicate_detection_enabled BOOLEAN NOT NULL DEFAULT TRUE
);

-- 2. FILES Table
CREATE TABLE IF NOT EXISTS files (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    file_name VARCHAR(255) NOT NULL,
    file_path VARCHAR(1024) NOT NULL,
    file_size BIGINT NOT NULL,
    file_type VARCHAR(50),
    category VARCHAR(50) NOT NULL,
    status VARCHAR(20) NOT NULL,
    hash VARCHAR(64) NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    modified_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    expiry_date DATE,
    last_accessed DATETIME,
    is_in_bin BOOLEAN NOT NULL DEFAULT FALSE,
    INDEX idx_files_hash (hash),
    INDEX idx_files_category (category),
    INDEX idx_files_status (status),
    INDEX idx_files_expiry (expiry_date),
    INDEX idx_files_is_in_bin (is_in_bin)
);

-- 3. BIN_FILES Table
CREATE TABLE IF NOT EXISTS bin_files (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    file_id BIGINT NOT NULL UNIQUE,
    original_path VARCHAR(1024) NOT NULL,
    deleted_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    original_status VARCHAR(20) NOT NULL,
    original_expiry_date DATE,
    CONSTRAINT fk_bin_file FOREIGN KEY (file_id) REFERENCES files (id) ON DELETE CASCADE
);

-- 4. NOTIFICATIONS Table
CREATE TABLE IF NOT EXISTS notifications (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    message TEXT NOT NULL,
    type VARCHAR(50) NOT NULL,
    file_id BIGINT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    is_read BOOLEAN NOT NULL DEFAULT FALSE,
    INDEX idx_notifications_is_read (is_read),
    INDEX idx_notifications_created_at (created_at)
);
