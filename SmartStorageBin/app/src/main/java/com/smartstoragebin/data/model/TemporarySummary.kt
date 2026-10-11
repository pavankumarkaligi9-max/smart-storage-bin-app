package com.smartstoragebin.data.model

data class TemporarySummary(
    val totalFiles: Int,
    val totalSize: Long,
    val expiredCount: Int,
    val expiringTodayCount: Int,
    val expiringSoonCount: Int
)
