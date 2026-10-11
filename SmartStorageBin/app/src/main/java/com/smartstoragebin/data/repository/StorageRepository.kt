package com.smartstoragebin.data.repository

import com.smartstoragebin.data.api.ApiResult
import com.smartstoragebin.data.api.ApiService
import com.smartstoragebin.data.model.Category
import com.smartstoragebin.data.model.DuplicateGroup
import com.smartstoragebin.data.model.StorageInfo
import com.smartstoragebin.data.sample.SampleData
import kotlinx.coroutines.delay

class StorageRepository(private val apiService: ApiService) {
    private val useSampleData = true

    suspend fun getStorageInfo(): ApiResult<StorageInfo> {
        return if (useSampleData) {
            delay(500)
            ApiResult.Success(SampleData.storageInfo)
        } else {
            try {
                val response = apiService.getStorageInfo()
                if (response.isSuccessful && response.body() != null) {
                    ApiResult.Success(response.body()!!)
                } else {
                    ApiResult.Error("Failed to fetch storage info", response.code())
                }
            } catch (e: Exception) {
                ApiResult.Error(e.message ?: "Unknown error")
            }
        }
    }

    suspend fun getCategories(): ApiResult<List<Category>> {
        return if (useSampleData) {
            delay(500)
            ApiResult.Success(SampleData.categories)
        } else {
            try {
                val response = apiService.getCategories()
                if (response.isSuccessful) {
                    ApiResult.Success(response.body() ?: emptyList())
                } else {
                    ApiResult.Error("Failed to fetch categories", response.code())
                }
            } catch (e: Exception) {
                ApiResult.Error(e.message ?: "Unknown error")
            }
        }
    }

    suspend fun getDuplicates(): ApiResult<List<DuplicateGroup>> {
        return if (useSampleData) {
            delay(500)
            ApiResult.Success(SampleData.duplicates)
        } else {
            try {
                val response = apiService.getDuplicates()
                if (response.isSuccessful) {
                    ApiResult.Success(response.body() ?: emptyList())
                } else {
                    ApiResult.Error("Failed to fetch duplicates", response.code())
                }
            } catch (e: Exception) {
                ApiResult.Error(e.message ?: "Unknown error")
            }
        }
    }
}
