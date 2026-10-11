package com.smartstoragebin.ui.screens.files

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smartstoragebin.data.api.ApiResult
import com.smartstoragebin.data.model.FileItem
import com.smartstoragebin.data.repository.FileRepository
import com.smartstoragebin.ui.screens.UiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FileDetailViewModel : ViewModel() {
    private val repository = FileRepository()

    private val _fileDetail = MutableStateFlow<UiState<FileItem>>(UiState.Loading)
    val fileDetail: StateFlow<UiState<FileItem>> = _fileDetail.asStateFlow()

    private val _showDeleteDialog = MutableStateFlow(false)
    val showDeleteDialog: StateFlow<Boolean> = _showDeleteDialog.asStateFlow()

    private val _showRenameDialog = MutableStateFlow(false)
    val showRenameDialog: StateFlow<Boolean> = _showRenameDialog.asStateFlow()

    private val _actionResult = MutableSharedFlow<String>()
    val actionResult: SharedFlow<String> = _actionResult.asSharedFlow()

    private var currentFileId: Long = -1L

    fun loadFile(id: Long) {
        currentFileId = id
        viewModelScope.launch {
            _fileDetail.value = UiState.Loading
            when (val res = repository.getFileById(id)) {
                is ApiResult.Success -> _fileDetail.value = UiState.Success(res.data)
                is ApiResult.Error -> _fileDetail.value = UiState.Error(res.message)
                is ApiResult.Loading -> _fileDetail.value = UiState.Loading
            }
        }
    }

    fun showDeleteDialog() { _showDeleteDialog.value = true }
    fun dismissDeleteDialog() { _showDeleteDialog.value = false }
    
    fun showRenameDialog() { _showRenameDialog.value = true }
    fun dismissRenameDialog() { _showRenameDialog.value = false }

    fun deleteFile() {
        viewModelScope.launch {
            dismissDeleteDialog()
            when (val res = repository.deleteFile(currentFileId)) {
                is ApiResult.Success -> _actionResult.emit("File deleted successfully")
                is ApiResult.Error -> _actionResult.emit("Failed to delete: ${res.message}")
                is ApiResult.Loading -> {}
            }
        }
    }

    fun renameFile(newName: String) {
        viewModelScope.launch {
            dismissRenameDialog()
            val fileState = _fileDetail.value
            if (fileState is UiState.Success) {
                val updatedFile = fileState.data.copy(name = newName)
                when (val res = repository.updateFile(currentFileId, updatedFile)) {
                    is ApiResult.Success -> {
                        _actionResult.emit("File renamed")
                        loadFile(currentFileId)
                    }
                    is ApiResult.Error -> _actionResult.emit("Failed to rename: ${res.message}")
                    is ApiResult.Loading -> {}
                }
            }
        }
    }

    fun shareFile() {
        viewModelScope.launch { _actionResult.emit("Sharing file...") }
    }

    fun openFile() {
        viewModelScope.launch { _actionResult.emit("Opening file...") }
    }

    fun moveFile() {
        viewModelScope.launch { _actionResult.emit("Move dialog not implemented yet") }
    }

    fun copyFile() {
        viewModelScope.launch { _actionResult.emit("Copy dialog not implemented yet") }
    }

    fun extendFile(newExpiry: String) {
        viewModelScope.launch {
            when (val res = repository.extendFile(currentFileId, newExpiry)) {
                is ApiResult.Success -> {
                    _actionResult.emit("Expiry extended")
                    loadFile(currentFileId)
                }
                is ApiResult.Error -> _actionResult.emit("Failed to extend: ${res.message}")
                is ApiResult.Loading -> {}
            }
        }
    }

    fun convertToPermanent() {
        viewModelScope.launch {
            when (val res = repository.convertToPermanent(currentFileId)) {
                is ApiResult.Success -> {
                    _actionResult.emit("Converted to permanent")
                    loadFile(currentFileId)
                }
                is ApiResult.Error -> _actionResult.emit("Failed to convert: ${res.message}")
                is ApiResult.Loading -> {}
            }
        }
    }
}
