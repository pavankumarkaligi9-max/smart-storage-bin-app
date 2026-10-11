package com.smartstoragebin.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smartstoragebin.data.api.ApiResult
import com.smartstoragebin.data.model.Category
import com.smartstoragebin.data.model.DuplicateGroup
import com.smartstoragebin.data.model.FileItem
import com.smartstoragebin.data.model.StorageInfo
import com.smartstoragebin.data.model.TemporarySummary
import com.smartstoragebin.data.repository.FileRepository
import com.smartstoragebin.data.repository.StorageRepository
import com.smartstoragebin.ui.screens.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val fileRepository = FileRepository()
    private val storageRepository = StorageRepository()

    private val _storageInfo = MutableStateFlow<UiState<StorageInfo>>(UiState.Loading)
    val storageInfo: StateFlow<UiState<StorageInfo>> = _storageInfo.asStateFlow()

    private val _temporarySummary = MutableStateFlow<UiState<TemporarySummary>>(UiState.Loading)
    val temporarySummary: StateFlow<UiState<TemporarySummary>> = _temporarySummary.asStateFlow()

    private val _categories = MutableStateFlow<UiState<List<Category>>>(UiState.Loading)
    val categories: StateFlow<UiState<List<Category>>> = _categories.asStateFlow()

    private val _recentFiles = MutableStateFlow<UiState<List<FileItem>>>(UiState.Loading)
    val recentFiles: StateFlow<UiState<List<FileItem>>> = _recentFiles.asStateFlow()

    private val _duplicates = MutableStateFlow<UiState<List<DuplicateGroup>>>(UiState.Loading)
    val duplicates: StateFlow<UiState<List<DuplicateGroup>>> = _duplicates.asStateFlow()

    fun loadDashboardData() {
        viewModelScope.launch {
            _storageInfo.value = UiState.Loading
            when (val res = storageRepository.getStorageInfo()) {
                is ApiResult.Success -> _storageInfo.value = UiState.Success(res.data)
                is ApiResult.Error -> _storageInfo.value = UiState.Error(res.message)
                is ApiResult.Loading -> _storageInfo.value = UiState.Loading
            }

            _temporarySummary.value = UiState.Loading
            when (val res = fileRepository.getTemporarySummary()) {
                is ApiResult.Success -> _temporarySummary.value = UiState.Success(res.data)
                is ApiResult.Error -> _temporarySummary.value = UiState.Error(res.message)
                is ApiResult.Loading -> _temporarySummary.value = UiState.Loading
            }

            _categories.value = UiState.Loading
            when (val res = storageRepository.getCategories()) {
                is ApiResult.Success -> _categories.value = UiState.Success(res.data)
                is ApiResult.Error -> _categories.value = UiState.Error(res.message)
                is ApiResult.Loading -> _categories.value = UiState.Loading
            }

            _recentFiles.value = UiState.Loading
            when (val res = fileRepository.getAllFiles()) { // Assuming recent files are top from all files
                is ApiResult.Success -> _recentFiles.value = UiState.Success(res.data.take(5))
                is ApiResult.Error -> _recentFiles.value = UiState.Error(res.message)
                is ApiResult.Loading -> _recentFiles.value = UiState.Loading
            }

            _duplicates.value = UiState.Loading
            when (val res = fileRepository.getDuplicates()) {
                is ApiResult.Success -> _duplicates.value = UiState.Success(res.data)
                is ApiResult.Error -> _duplicates.value = UiState.Error(res.message)
                is ApiResult.Loading -> _duplicates.value = UiState.Loading
            }
        }
    }
}
