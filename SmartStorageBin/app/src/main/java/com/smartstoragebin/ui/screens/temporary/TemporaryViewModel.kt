package com.smartstoragebin.ui.screens.temporary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smartstoragebin.data.model.*
import com.smartstoragebin.ui.screens.UiState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

enum class TemporaryFilter { ALL, ACTIVE, EXPIRING_SOON, EXPIRED }

class TemporaryViewModel : ViewModel() {

    private val _temporaryFiles = MutableStateFlow<UiState<List<FileItem>>>(UiState.Loading)
    val temporaryFiles: StateFlow<UiState<List<FileItem>>> = _temporaryFiles.asStateFlow()

    private val _temporarySummary = MutableStateFlow<UiState<TemporarySummary>>(UiState.Loading)
    val temporarySummary: StateFlow<UiState<TemporarySummary>> = _temporarySummary.asStateFlow()

    private val _selectedFilter = MutableStateFlow(TemporaryFilter.ALL)
    val selectedFilter: StateFlow<TemporaryFilter> = _selectedFilter.asStateFlow()

    private val _showExpiredDialog = MutableStateFlow<FileItem?>(null)
    val showExpiredDialog: StateFlow<FileItem?> = _showExpiredDialog.asStateFlow()

    private val _showExtendDialog = MutableStateFlow<FileItem?>(null)
    val showExtendDialog: StateFlow<FileItem?> = _showExtendDialog.asStateFlow()

    private val _actionResult = MutableSharedFlow<String>()
    val actionResult: SharedFlow<String> = _actionResult.asSharedFlow()

    init {
        loadTemporaryFiles()
    }

    fun loadTemporaryFiles() {
        viewModelScope.launch {
            _temporaryFiles.value = UiState.Loading
            // Mock data
            val files = listOf(
                FileItem(1L, "temp_doc.pdf", "/temp/", 1024L, "pdf", "Documents", FileStatus.TEMPORARY, "today", "tomorrow", "hash", false),
                FileItem(2L, "temp_img.jpg", "/temp/", 2048L, "jpg", "Images", FileStatus.TEMPORARY, "yesterday", "today", "hash", false)
            )
            _temporaryFiles.value = UiState.Success(files)
            _temporarySummary.value = UiState.Success(TemporarySummary(2, 3072L, 0, 1, 1))
        }
    }

    fun setFilter(filter: TemporaryFilter) {
        _selectedFilter.value = filter
        // Re-filter list based on filter
    }

    fun extendFile(id: Long, newExpiry: String) {
        viewModelScope.launch {
            _actionResult.emit("File expiry extended")
        }
    }

    fun convertToPermanent(id: Long) {
        viewModelScope.launch {
            _actionResult.emit("File converted to permanent")
        }
    }

    fun deleteFile(id: Long) {
        viewModelScope.launch {
            _actionResult.emit("File deleted")
        }
    }

    fun showExpiredDialogFor(file: FileItem) {
        _showExpiredDialog.value = file
    }

    fun showExtendDialogFor(file: FileItem) {
        _showExtendDialog.value = file
    }

    fun dismissDialogs() {
        _showExpiredDialog.value = null
        _showExtendDialog.value = null
    }

    fun refreshData() {
        loadTemporaryFiles()
    }
}
