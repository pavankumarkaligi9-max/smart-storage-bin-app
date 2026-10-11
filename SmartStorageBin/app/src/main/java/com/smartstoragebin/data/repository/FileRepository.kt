package com.smartstoragebin.data.repository

import com.smartstoragebin.data.api.ApiResult
import com.smartstoragebin.data.api.ApiService
import com.smartstoragebin.data.model.FileItem
import com.smartstoragebin.data.model.TemporarySummary
import com.smartstoragebin.data.sample.SampleData
import com.smartstoragebin.data.model.remainingDays
import kotlinx.coroutines.delay

class FileRepository(private val apiService: ApiService) {
    private val useSampleData = true

    suspend fun getFiles(): ApiResult<List<FileItem>> {
        return if (useSampleData) {
            delay(500)
            ApiResult.Success(SampleData.files.filter { !it.isInBin })
        } else {
            try {
                val response = apiService.getFiles()
                if (response.isSuccessful) {
                    ApiResult.Success(response.body() ?: emptyList())
                } else {
                    ApiResult.Error("Failed to fetch files", response.code())
                }
            } catch (e: Exception) {
                ApiResult.Error(e.message ?: "Unknown error")
            }
        }
    }

    suspend fun getFileById(id: Long): ApiResult<FileItem> {
        return if (useSampleData) {
            val file = SampleData.files.find { it.id == id }
            if (file != null) ApiResult.Success(file) else ApiResult.Error("File not found")
        } else {
            try {
                val response = apiService.getFileById(id)
                if (response.isSuccessful && response.body() != null) {
                    ApiResult.Success(response.body()!!)
                } else {
                    ApiResult.Error("Failed to fetch file", response.code())
                }
            } catch (e: Exception) {
                ApiResult.Error(e.message ?: "Unknown error")
            }
        }
    }

    suspend fun createFile(file: FileItem): ApiResult<FileItem> {
        return if (useSampleData) {
            ApiResult.Success(file)
        } else {
            try {
                val response = apiService.createFile(file)
                if (response.isSuccessful && response.body() != null) {
                    ApiResult.Success(response.body()!!)
                } else {
                    ApiResult.Error("Failed to create file", response.code())
                }
            } catch (e: Exception) {
                ApiResult.Error(e.message ?: "Unknown error")
            }
        }
    }

    suspend fun updateFile(id: Long, file: FileItem): ApiResult<FileItem> {
        return if (useSampleData) {
            ApiResult.Success(file)
        } else {
            try {
                val response = apiService.updateFile(id, file)
                if (response.isSuccessful && response.body() != null) {
                    ApiResult.Success(response.body()!!)
                } else {
                    ApiResult.Error("Failed to update file", response.code())
                }
            } catch (e: Exception) {
                ApiResult.Error(e.message ?: "Unknown error")
            }
        }
    }

    suspend fun deleteFile(id: Long): ApiResult<Unit> {
        return if (useSampleData) {
            ApiResult.Success(Unit)
        } else {
            try {
                val response = apiService.deleteFile(id)
                if (response.isSuccessful) {
                    ApiResult.Success(Unit)
                } else {
                    ApiResult.Error("Failed to delete file", response.code())
                }
            } catch (e: Exception) {
                ApiResult.Error(e.message ?: "Unknown error")
            }
        }
    }

    suspend fun getTemporaryFiles(): ApiResult<List<FileItem>> {
        return if (useSampleData) {
            delay(500)
            ApiResult.Success(SampleData.files.filter { it.status == com.smartstoragebin.data.model.FileStatus.TEMPORARY && !it.isInBin })
        } else {
            try {
                val response = apiService.getTemporaryFiles()
                if (response.isSuccessful) {
                    ApiResult.Success(response.body() ?: emptyList())
                } else {
                    ApiResult.Error("Failed to fetch temporary files", response.code())
                }
            } catch (e: Exception) {
                ApiResult.Error(e.message ?: "Unknown error")
            }
        }
    }

    suspend fun getPermanentFiles(): ApiResult<List<FileItem>> {
        return if (useSampleData) {
            delay(500)
            ApiResult.Success(SampleData.files.filter { it.status == com.smartstoragebin.data.model.FileStatus.PERMANENT && !it.isInBin })
        } else {
            try {
                val response = apiService.getPermanentFiles()
                if (response.isSuccessful) {
                    ApiResult.Success(response.body() ?: emptyList())
                } else {
                    ApiResult.Error("Failed to fetch permanent files", response.code())
                }
            } catch (e: Exception) {
                ApiResult.Error(e.message ?: "Unknown error")
            }
        }
    }

    suspend fun getExpiredFiles(): ApiResult<List<FileItem>> {
        return if (useSampleData) {
            delay(500)
            val expired = SampleData.files.filter { 
                it.status == com.smartstoragebin.data.model.FileStatus.TEMPORARY && 
                !it.isInBin &&
                it.remainingDays() != null && 
                it.remainingDays()!! <= 0 
            }
            ApiResult.Success(expired)
        } else {
            try {
                val response = apiService.getExpiredFiles()
                if (response.isSuccessful) {
                    ApiResult.Success(response.body() ?: emptyList())
                } else {
                    ApiResult.Error("Failed to fetch expired files", response.code())
                }
            } catch (e: Exception) {
                ApiResult.Error(e.message ?: "Unknown error")
            }
        }
    }

    suspend fun extendFile(id: Long, newExpiry: String): ApiResult<FileItem> {
        return if (useSampleData) {
            val file = SampleData.files.find { it.id == id }?.copy(expiryDate = newExpiry)
            if (file != null) ApiResult.Success(file) else ApiResult.Error("File not found")
        } else {
            try {
                val map = mapOf("expiryDate" to newExpiry)
                val response = apiService.extendFile(id, map)
                if (response.isSuccessful && response.body() != null) {
                    ApiResult.Success(response.body()!!)
                } else {
                    ApiResult.Error("Failed to extend file", response.code())
                }
            } catch (e: Exception) {
                ApiResult.Error(e.message ?: "Unknown error")
            }
        }
    }

    suspend fun convertToPermanent(id: Long): ApiResult<FileItem> {
        return if (useSampleData) {
            val file = SampleData.files.find { it.id == id }?.copy(status = com.smartstoragebin.data.model.FileStatus.PERMANENT, expiryDate = null)
            if (file != null) ApiResult.Success(file) else ApiResult.Error("File not found")
        } else {
            try {
                val response = apiService.convertToPermanent(id)
                if (response.isSuccessful && response.body() != null) {
                    ApiResult.Success(response.body()!!)
                } else {
                    ApiResult.Error("Failed to convert file", response.code())
                }
            } catch (e: Exception) {
                ApiResult.Error(e.message ?: "Unknown error")
            }
        }
    }

    suspend fun searchFiles(query: String, type: String? = null, status: String? = null, category: String? = null): ApiResult<List<FileItem>> {
        return if (useSampleData) {
            delay(500)
            val filtered = SampleData.files.filter { 
                !it.isInBin &&
                it.name.contains(query, ignoreCase = true) &&
                (type == null || it.type.equals(type, ignoreCase = true)) &&
                (status == null || it.status.name.equals(status, ignoreCase = true)) &&
                (category == null || it.category.equals(category, ignoreCase = true))
            }
            ApiResult.Success(filtered)
        } else {
            try {
                val response = apiService.searchFiles(query, type, status, category)
                if (response.isSuccessful) {
                    ApiResult.Success(response.body() ?: emptyList())
                } else {
                    ApiResult.Error("Failed to search files", response.code())
                }
            } catch (e: Exception) {
                ApiResult.Error(e.message ?: "Unknown error")
            }
        }
    }

    suspend fun getTemporarySummary(): ApiResult<TemporarySummary> {
        return if (useSampleData) {
            delay(500)
            ApiResult.Success(SampleData.temporarySummary)
        } else {
            try {
                val response = apiService.getTemporarySummary()
                if (response.isSuccessful && response.body() != null) {
                    ApiResult.Success(response.body()!!)
                } else {
                    ApiResult.Error("Failed to fetch summary", response.code())
                }
            } catch (e: Exception) {
                ApiResult.Error(e.message ?: "Unknown error")
            }
        }
    }
}
