package com.smartstoragebin.data.sample

import com.smartstoragebin.data.model.Category
import com.smartstoragebin.data.model.DuplicateGroup
import com.smartstoragebin.data.model.FileItem
import com.smartstoragebin.data.model.FileStatus
import com.smartstoragebin.data.model.StorageInfo
import com.smartstoragebin.data.model.TemporarySummary
import java.time.LocalDate

object SampleData {
    val files = listOf(
        FileItem(1, "DBMS Notes.pdf", "/storage/emulated/0/Download/DBMS Notes.pdf", 4500000, "PDF", "Documents", FileStatus.TEMPORARY, "2026-10-01", "2026-10-05T00:00:00", "hash1", false, null, null),
        FileItem(2, "OS Lecture.mp4", "/storage/emulated/0/Movies/OS Lecture.mp4", 512000000, "MP4", "Videos", FileStatus.PERMANENT, "2026-09-15", null, "hash2", false, null, null),
        FileItem(3, "Vacation Photo.jpg", "/storage/emulated/0/DCIM/Camera/Vacation Photo.jpg", 3200000, "JPG", "Images", FileStatus.PERMANENT, "2026-08-20", null, "hash3", false, null, null),
        FileItem(4, "Assignment 1.docx", "/storage/emulated/0/Documents/Assignment 1.docx", 1200000, "DOCX", "Documents", FileStatus.TEMPORARY, "2026-09-25", "2026-10-03T00:00:00", "hash4", false, null, null),
        FileItem(5, "Old Notes.pdf", "/storage/emulated/0/Download/Old Notes.pdf", 5400000, "PDF", "Documents", FileStatus.TEMPORARY, "2026-05-10", "2026-09-01T00:00:00", "hash5", false, null, null),
        FileItem(6, "Screenshot_1.png", "/storage/emulated/0/Pictures/Screenshots/Screenshot_1.png", 2500000, "PNG", "Images", FileStatus.TEMPORARY, "2026-10-02", "2026-10-04T00:00:00", "hash6", false, null, null),
        FileItem(7, "Music Track.mp3", "/storage/emulated/0/Music/Music Track.mp3", 6500000, "MP3", "Audio", FileStatus.PERMANENT, "2026-07-11", null, "hash7", false, null, null),
        FileItem(8, "Project Archive.zip", "/storage/emulated/0/Download/Project Archive.zip", 24000000, "ZIP", "Archives", FileStatus.PERMANENT, "2026-09-28", null, "hash8", false, null, null),
        FileItem(9, "App APK.apk", "/storage/emulated/0/Download/App APK.apk", 35000000, "APK", "Applications", FileStatus.PERMANENT, "2026-09-30", null, "hash9", false, null, null),
        FileItem(10, "Unknown File.dat", "/storage/emulated/0/Download/Unknown File.dat", 1024000, "DAT", "Other", FileStatus.TEMPORARY, "2026-10-01", "2026-10-10T00:00:00", "hash10", false, null, null),
        FileItem(11, "Deleted Image.jpg", "/storage/emulated/0/DCIM/Camera/Deleted Image.jpg", 4000000, "JPG", "Images", FileStatus.PERMANENT, "2026-09-01", null, "hash11", true, "2026-10-01T12:00:00", "/storage/emulated/0/DCIM/Camera/Deleted Image.jpg"),
        FileItem(12, "Duplicate Video 1.mp4", "/storage/emulated/0/Movies/Dup 1.mp4", 150000000, "MP4", "Videos", FileStatus.PERMANENT, "2026-09-10", null, "hash_dup_1", false, null, null),
        FileItem(13, "Duplicate Video 2.mp4", "/storage/emulated/0/Download/Dup 2.mp4", 150000000, "MP4", "Videos", FileStatus.PERMANENT, "2026-09-12", null, "hash_dup_1", false, null, null),
        FileItem(14, "Notes 2.pdf", "/storage/emulated/0/Documents/Notes 2.pdf", 4500000, "PDF", "Documents", FileStatus.TEMPORARY, "2026-10-02", LocalDate.now().plusDays(2).toString() + "T00:00:00", "hash14", false, null, null),
        FileItem(15, "Notes 3.pdf", "/storage/emulated/0/Documents/Notes 3.pdf", 4500000, "PDF", "Documents", FileStatus.TEMPORARY, "2026-10-02", LocalDate.now().plusDays(1).toString() + "T00:00:00", "hash15", false, null, null)
    )

    val storageInfo = StorageInfo(
        totalStorage = 128000000000, // 128 GB
        usedStorage = 96000000000,   // 96 GB
        freeStorage = 32000000000,   // 32 GB
        temporaryStorage = 15000000000,
        duplicateStorage = 5000000000,
        binStorage = 1000000000
    )

    val categories = listOf(
        Category("Documents", 45, 1200000000, "icon_document"),
        Category("Images", 1500, 15000000000, "icon_image"),
        Category("Videos", 120, 45000000000, "icon_video"),
        Category("Audio", 300, 3000000000, "icon_audio"),
        Category("Archives", 20, 5000000000, "icon_archive"),
        Category("Applications", 50, 10000000000, "icon_application"),
        Category("Other", 200, 1500000000, "icon_file")
    )

    val duplicates = listOf(
        DuplicateGroup("hash_dup_1", listOf(files[11], files[12]), 150000000)
    )

    val temporarySummary = TemporarySummary(
        totalFiles = 250,
        totalSize = 15000000000,
        expiredCount = 45,
        expiringTodayCount = 12,
        expiringSoonCount = 30
    )
}
