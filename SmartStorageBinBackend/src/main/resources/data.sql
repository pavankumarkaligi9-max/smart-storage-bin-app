-- Smart Storage Bin Initial Seed Data

-- 1. Default Settings
INSERT INTO settings (id, notification_enabled, default_expiry_days, expiry_warning_days, bin_retention_days, duplicate_detection_enabled)
VALUES (1, TRUE, 7, 3, 30, TRUE)
ON DUPLICATE KEY UPDATE id=id;

-- 2. Files Seed Data
INSERT INTO files (id, file_name, file_path, file_size, file_type, category, status, hash, created_at, modified_at, expiry_date, last_accessed, is_in_bin)
VALUES 
(1, 'DBMS Notes.pdf', '/storage/emulated/0/Download/DBMS Notes.pdf', 4500000, 'PDF', 'DOCUMENTS', 'TEMPORARY', 'e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855', NOW(), NOW(), DATE_ADD(CURRENT_DATE, INTERVAL 3 DAY), NOW(), FALSE),
(2, 'OS Lecture.mp4', '/storage/emulated/0/Movies/OS Lecture.mp4', 512000000, 'MP4', 'VIDEOS', 'PERMANENT', 'a1b2c3d4e5f67890123456789abcdef0123456789abcdef0123456789abcdef0', NOW(), NOW(), NULL, NOW(), FALSE),
(3, 'Vacation Photo.jpg', '/storage/emulated/0/DCIM/Camera/Vacation Photo.jpg', 3200000, 'JPG', 'IMAGES', 'PERMANENT', 'b2c3d4e5f6a17890123456789abcdef0123456789abcdef0123456789abcdef1', NOW(), NOW(), NULL, NOW(), FALSE),
(4, 'Assignment 1.docx', '/storage/emulated/0/Documents/Assignment 1.docx', 1200000, 'DOCX', 'DOCUMENTS', 'TEMPORARY', 'c3d4e5f6a1b27890123456789abcdef0123456789abcdef0123456789abcdef2', NOW(), NOW(), DATE_ADD(CURRENT_DATE, INTERVAL 1 DAY), NOW(), FALSE),
(5, 'Old Notes.pdf', '/storage/emulated/0/Download/Old Notes.pdf', 5400000, 'PDF', 'DOCUMENTS', 'TEMPORARY', 'd4e5f6a1b2c37890123456789abcdef0123456789abcdef0123456789abcdef3', NOW(), NOW(), DATE_SUB(CURRENT_DATE, INTERVAL 2 DAY), NOW(), FALSE),
(6, 'Screenshot_1.png', '/storage/emulated/0/Pictures/Screenshots/Screenshot_1.png', 2500000, 'PNG', 'IMAGES', 'TEMPORARY', 'e5f6a1b2c3d47890123456789abcdef0123456789abcdef0123456789abcdef4', NOW(), NOW(), DATE_ADD(CURRENT_DATE, INTERVAL 2 DAY), NOW(), FALSE),
(7, 'Music Track.mp3', '/storage/emulated/0/Music/Music Track.mp3', 6500000, 'MP3', 'AUDIO', 'PERMANENT', 'f6a1b2c3d4e57890123456789abcdef0123456789abcdef0123456789abcdef5', NOW(), NOW(), NULL, NOW(), FALSE),
(8, 'Project Archive.zip', '/storage/emulated/0/Download/Project Archive.zip', 24000000, 'ZIP', 'ARCHIVES', 'PERMANENT', 'a7b2c3d4e5f67890123456789abcdef0123456789abcdef0123456789abcdef6', NOW(), NOW(), NULL, NOW(), FALSE),
(9, 'App APK.apk', '/storage/emulated/0/Download/App APK.apk', 35000000, 'APK', 'APPLICATIONS', 'PERMANENT', 'b8c3d4e5f6a17890123456789abcdef0123456789abcdef0123456789abcdef7', NOW(), NOW(), NULL, NOW(), FALSE),
(10, 'Deleted Image.jpg', '/storage/emulated/0/DCIM/Camera/Deleted Image.jpg', 4000000, 'JPG', 'IMAGES', 'PERMANENT', 'c9d4e5f6a1b27890123456789abcdef0123456789abcdef0123456789abcdef8', NOW(), NOW(), NULL, NOW(), TRUE),
(11, 'Duplicate Video 1.mp4', '/storage/emulated/0/Movies/Dup 1.mp4', 150000000, 'MP4', 'VIDEOS', 'PERMANENT', 'hash_dup_video_123456789abcdef0123456789abcdef0123456789abcdef00', NOW(), NOW(), NULL, NOW(), FALSE),
(12, 'Duplicate Video 2.mp4', '/storage/emulated/0/Download/Dup 2.mp4', 150000000, 'MP4', 'VIDEOS', 'PERMANENT', 'hash_dup_video_123456789abcdef0123456789abcdef0123456789abcdef00', NOW(), NOW(), NULL, NOW(), FALSE)
ON DUPLICATE KEY UPDATE id=id;

-- 3. Bin Files Seed Data
INSERT INTO bin_files (id, file_id, original_path, deleted_at, original_status, original_expiry_date)
VALUES (1, 10, '/storage/emulated/0/DCIM/Camera/Deleted Image.jpg', NOW(), 'PERMANENT', NULL)
ON DUPLICATE KEY UPDATE id=id;

-- 4. Notifications Seed Data
INSERT INTO notifications (id, title, message, type, file_id, created_at, is_read)
VALUES
(1, 'Temporary File Expired', 'The temporary storage period for Old Notes.pdf has ended.', 'EXPIRED', 5, NOW(), FALSE),
(2, 'File Expiring Soon', 'DBMS Notes.pdf will expire in 3 days.', 'EXPIRING_SOON', 1, NOW(), FALSE),
(3, 'Duplicate Detected', 'Duplicate file detected for Duplicate Video 2.mp4 (matches Dup 1.mp4)', 'DUPLICATE_DETECTED', 12, NOW(), TRUE)
ON DUPLICATE KEY UPDATE id=id;
