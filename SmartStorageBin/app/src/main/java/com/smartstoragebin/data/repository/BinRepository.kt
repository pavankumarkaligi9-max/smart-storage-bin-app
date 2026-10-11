package com.smartstoragebin.data.repository

import com.smartstoragebin.data.api.ApiResult
import com.smartstoragebin.data.api.ApiService
import com.smartstoragebin.data.model.FileItem
import com.smartstoragebin.data.sample.SampleData
import kotlinx.coroutines.delay

class BinRepository(private val apiService: ApiService) {
    private val useSampleData = true

    suspend fun getBinFiles(): ApiResult<List<FileItem>> {
        return if (useSampleData) {
            delay(500)
            ApiResult.Success(SampleData.files.filter { it.isInBin })
        } else {
            try {
                val response = apiService.getBinFiles()
                if (response.isSuccessful) {
                    ApiResult.Success(response.body() ?: emptyList())
                } else {
                    ApiResult.Error("Failed to fetch bin files", response.code())
                }
            } catch (e: Exception) {
                ApiResult.Error(e.message ?: "Unknown error")
            }
        }
    }

    suspend fun restoreFromBin(id: Long): ApiResult<FileItem> {
        return if (useSampleData) {
            val file = SampleData.files.find { it.id == id }?.copy(isInBin = false, deletedAt = null)
            if (file != null) ApiResult.Success(file) else ApiResult.Error("File not found in bin")
        } else {
            try {
                val response = apiService.restoreFromBin(id)
                if (response.isSuccessful && response.body() != null) {
                    ApiResult.Success(response.body()!!)
                } else {
                    ApiResult.Error("Failed to restore file", response.code())
                }
            } catch (e: Exception) {
                ApiResult.Error(e.message ?: "Unknown error")
            }
        }
    }

    suspend fun deleteFromBin(id: Long): ApiResult<Unit> {
        return if (useSampleData) {
            ApiResult.Success(Unit)
        } else {
            try {
                val response = apiService.deleteFromBin(id)
                if (response.isSuccessful) {
                    ApiResult.Success(Unit)
                } else {
                    ApiResult.Error("Failed to delete file from bin", response.code())
                }
            } catch (e: Exception) {
                ApiResult.Error(e.message ?: "Unknown error")
            }
        }
    }
}
