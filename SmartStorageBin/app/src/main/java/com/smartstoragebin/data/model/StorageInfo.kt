package com.smartstoragebin.data.model

import kotlin.math.ln
import kotlin.math.pow

data class StorageInfo(
    val totalStorage: Long,
    val usedStorage: Long,
    val freeStorage: Long,
    val temporaryStorage: Long,
    val duplicateStorage: Long,
    val binStorage: Long
)

fun Long.formatStorageSize(): String {
    if (this < 1024) return "$this B"
    val exp = (ln(this.toDouble()) / ln(1024.0)).toInt()
    val pre = "KMGTPE"[exp - 1]
    return String.format("%.1f %sB", this / 1024.0.pow(exp.toDouble()), pre)
}
