package com.smartstoragebin.ui.screens.storage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smartstoragebin.data.model.Category
import com.smartstoragebin.data.model.StorageInfo
import com.smartstoragebin.ui.screens.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class StorageViewModel : ViewModel() {

    private val _storageInfo = MutableStateFlow<UiState<StorageInfo>>(UiState.Loading)
    val storageInfo: StateFlow<UiState<StorageInfo>> = _storageInfo.asStateFlow()

    private val _categories = MutableStateFlow<UiState<List<Category>>>(UiState.Loading)
    val categories: StateFlow<UiState<List<Category>>> = _categories.asStateFlow()

    init {
        loadStorageData()
    }

    fun loadStorageData() {
        viewModelScope.launch {
            _storageInfo.value = UiState.Loading
            _categories.value = UiState.Loading
            
            // Mock data
            val info = StorageInfo(100000L, 60000L, 40000L, 5000L, 2000L, 1000L)
            _storageInfo.value = UiState.Success(info)

            val cats = listOf(
                Category("Documents", 25, 32000000L, "icon_doc"),
                Category("Images", 100, 150000000L, "icon_img"),
                Category("Videos", 10, 500000000L, "icon_vid")
            )
            _categories.value = UiState.Success(cats)
        }
    }

    fun refreshData() {
        loadStorageData()
    }
}
