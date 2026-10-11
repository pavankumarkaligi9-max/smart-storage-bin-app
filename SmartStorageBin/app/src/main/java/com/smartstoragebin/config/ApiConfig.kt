package com.smartstoragebin.config

object ApiConfig {
    const val BASE_URL = "http://10.0.2.2:8080"
    
    const val FILES = "/api/files"
    const val FILE_BY_ID = "/api/files/{id}"
    const val TEMPORARY_FILES = "/api/files/temporary"
    const val PERMANENT_FILES = "/api/files/permanent"
    const val EXPIRED_FILES = "/api/files/expired"
    const val EXTEND_FILE = "/api/files/{id}/extend"
    const val CONVERT_PERMANENT = "/api/files/{id}/convert-permanent"
    const val CATEGORIES = "/api/categories"
    const val STORAGE = "/api/storage"
    const val DUPLICATES = "/api/duplicates"
    const val BIN = "/api/bin"
    const val BIN_RESTORE = "/api/bin/{id}/restore"
    const val BIN_DELETE = "/api/bin/{id}"
    const val SEARCH = "/api/search"
    
    const val CONNECT_TIMEOUT = 30L
    const val READ_TIMEOUT = 30L
    const val WRITE_TIMEOUT = 30L
}
