package com.smartstoragebin.ui.screens.files

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smartstoragebin.data.api.ApiResult
import com.smartstoragebin.data.model.FileItem
import com.smartstoragebin.data.model.FileStatus
import com.smartstoragebin.data.repository.FileRepository
import com.smartstoragebin.ui.screens.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class SortOrder { NAME, SIZE, DATE, TYPE }
enum class ViewMode { LIST, GRID }

class FilesViewModel : ViewModel() {
    private val repository = FileRepository()

    private val _files = MutableStateFlow<UiState<List<FileItem>>>(UiState.Loading)
    val files: StateFlow<UiState<List<FileItem>>> = _files.asStateFlow()

    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory.asStateFlow()

    private val _sortOrder = MutableStateFlow(SortOrder.DATE)
    val sortOrder: StateFlow<SortOrder> = _sortOrder.asStateFlow()

    private val _viewMode = MutableStateFlow(ViewMode.LIST)
    val viewMode: StateFlow<ViewMode> = _viewMode.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _showClassificationDialog = MutableStateFlow(false)
    val showClassificationDialog: StateFlow<Boolean> = _showClassificationDialog.asStateFlow()

    private var allFiles: List<FileItem> = emptyList()

    fun loadFiles() {
        viewModelScope.launch {
            _files.value = UiState.Loading
            when (val res = repository.getAllFiles()) {
                is ApiResult.Success -> {
                    allFiles = res.data
                    applyFilters()
                }
                is ApiResult.Error -> _files.value = UiState.Error(res.message)
                is ApiResult.Loading -> _files.value = UiState.Loading
            }
        }
    }

    fun filterByCategory(category: String?) {
        _selectedCategory.value = category
        applyFilters()
    }

    fun setSortOrder(order: SortOrder) {
        _sortOrder.value = order
        applyFilters()
    }

    fun toggleViewMode() {
        _viewMode.value = if (_viewMode.value == ViewMode.LIST) ViewMode.GRID else ViewMode.LIST
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
        applyFilters()
    }

    fun showNewFileDialog() {
        _showClassificationDialog.value = true
    }

    fun dismissNewFileDialog() {
        _showClassificationDialog.value = false
    }

    fun classifyFile(fileId: Long, status: FileStatus, expiryDate: String?) {
        // Dummy implementation for dialog action
        loadFiles()
    }

    private fun applyFilters() {
        var filteredList = allFiles

        val category = _selectedCategory.value
        if (category != null) {
            filteredList = filteredList.filter { it.category == category }
        }

        val query = _searchQuery.value
        if (query.isNotBlank()) {
            filteredList = filteredList.filter { it.name.contains(query, ignoreCase = true) }
        }

        filteredList = when (_sortOrder.value) {
            SortOrder.NAME -> filteredList.sortedBy { it.name }
            SortOrder.SIZE -> filteredList.sortedByDescending { it.size }
            SortOrder.DATE -> filteredList.sortedByDescending { it.createdAt }
            SortOrder.TYPE -> filteredList.sortedBy { it.type }
        }

        _files.value = UiState.Success(filteredList)
    }
}
