package com.smartstoragebin.data.model

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import kotlin.math.ln
import kotlin.math.pow

enum class FileStatus { TEMPORARY, PERMANENT }
enum class ExpiryStatus { ACTIVE, EXPIRING_SOON, EXPIRED }

data class FileItem(
    val id: Long,
    val name: String,
    val path: String,
    val size: Long,
    val type: String,
    val category: String,
    val status: FileStatus,
    val createdAt: String,
    val expiryDate: String?,
    val hash: String,
    val isInBin: Boolean,
    val deletedAt: String? = null,
    val originalPath: String? = null
)

fun FileItem.formattedSize(): String {
    if (size < 1024) return "$size B"
    val exp = (ln(size.toDouble()) / ln(1024.0)).toInt()
    val pre = "KMGTPE"[exp - 1]
    return String.format("%.1f %sB", size / 1024.0.pow(exp.toDouble()), pre)
}

fun FileItem.remainingDays(): Int? {
    if (status == FileStatus.PERMANENT || expiryDate == null) return null
    return try {
        val formatter = DateTimeFormatter.ISO_LOCAL_DATE
        val expiry = LocalDate.parse(expiryDate.substringBefore("T"), formatter)
        val today = LocalDate.now()
        ChronoUnit.DAYS.between(today, expiry).toInt()
    } catch (e: Exception) {
        null
    }
}

fun FileItem.expiryStatus(): ExpiryStatus? {
    val days = remainingDays() ?: return null
    return when {
        days <= 0 -> ExpiryStatus.EXPIRED
        days <= 3 -> ExpiryStatus.EXPIRING_SOON
        else -> ExpiryStatus.ACTIVE
    }
}

fun FileItem.fileIcon(): String {
    return when (category.lowercase()) {
        "documents" -> "icon_document"
        "images" -> "icon_image"
        "videos" -> "icon_video"
        "audio" -> "icon_audio"
        "archives" -> "icon_archive"
        "applications" -> "icon_application"
        else -> "icon_file"
    }
}
