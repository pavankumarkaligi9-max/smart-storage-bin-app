package com.smartstoragebin.data.model

data class DuplicateGroup(
    val hash: String,
    val files: List<FileItem>,
    val totalWastedSpace: Long
)
