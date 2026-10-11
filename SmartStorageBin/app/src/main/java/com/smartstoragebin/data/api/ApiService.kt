package com.smartstoragebin.data.api

import com.smartstoragebin.data.model.Category
import com.smartstoragebin.data.model.DuplicateGroup
import com.smartstoragebin.data.model.FileItem
import com.smartstoragebin.data.model.StorageInfo
import com.smartstoragebin.data.model.TemporarySummary
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("files")
    suspend fun getFiles(): Response<List<FileItem>>

    @GET("files/{id}")
    suspend fun getFileById(@Path("id") id: Long): Response<FileItem>

    @POST("files")
    suspend fun createFile(@Body file: FileItem): Response<FileItem>

    @PUT("files/{id}")
    suspend fun updateFile(@Path("id") id: Long, @Body file: FileItem): Response<FileItem>

    @DELETE("files/{id}")
    suspend fun deleteFile(@Path("id") id: Long): Response<Unit>

    @GET("files/temporary")
    suspend fun getTemporaryFiles(): Response<List<FileItem>>

    @GET("files/permanent")
    suspend fun getPermanentFiles(): Response<List<FileItem>>

    @GET("files/expired")
    suspend fun getExpiredFiles(): Response<List<FileItem>>

    @PUT("files/{id}/extend")
    suspend fun extendFile(@Path("id") id: Long, @Body newExpiry: Map<String, String>): Response<FileItem>

    @PUT("files/{id}/convert")
    suspend fun convertToPermanent(@Path("id") id: Long): Response<FileItem>

    @GET("categories")
    suspend fun getCategories(): Response<List<Category>>

    @GET("storage")
    suspend fun getStorageInfo(): Response<StorageInfo>

    @GET("duplicates")
    suspend fun getDuplicates(): Response<List<DuplicateGroup>>

    @GET("bin")
    suspend fun getBinFiles(): Response<List<FileItem>>

    @POST("bin/{id}/restore")
    suspend fun restoreFromBin(@Path("id") id: Long): Response<FileItem>

    @DELETE("bin/{id}")
    suspend fun deleteFromBin(@Path("id") id: Long): Response<Unit>

    @GET("files/search")
    suspend fun searchFiles(
        @Query("query") query: String,
        @Query("type") type: String? = null,
        @Query("status") status: String? = null,
        @Query("category") category: String? = null
    ): Response<List<FileItem>>

    @GET("files/temporary/summary")
    suspend fun getTemporarySummary(): Response<TemporarySummary>
}
