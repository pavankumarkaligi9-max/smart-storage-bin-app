package com.smartstoragebin.ui.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smartstoragebin.data.model.FileItem
import com.smartstoragebin.ui.screens.UiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _searchResults = MutableStateFlow<UiState<List<FileItem>>>(UiState.Success(emptyList()))
    val searchResults: StateFlow<UiState<List<FileItem>>> = _searchResults.asStateFlow()

    private val _selectedStatusFilter = MutableStateFlow<String?>(null)
    val selectedStatusFilter: StateFlow<String?> = _selectedStatusFilter.asStateFlow()

    private val _selectedTypeFilter = MutableStateFlow<String?>(null)
    val selectedTypeFilter: StateFlow<String?> = _selectedTypeFilter.asStateFlow()

    private val _selectedCategoryFilter = MutableStateFlow<String?>(null)
    val selectedCategoryFilter: StateFlow<String?> = _selectedCategoryFilter.asStateFlow()

    private val _showFilters = MutableStateFlow(false)
    val showFilters: StateFlow<Boolean> = _showFilters.asStateFlow()

    private var searchJob: Job? = null

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(300) // debounce
            performSearch()
        }
    }

    fun setStatusFilter(status: String?) {
        _selectedStatusFilter.value = status
        performSearch()
    }

    fun setTypeFilter(type: String?) {
        _selectedTypeFilter.value = type
        performSearch()
    }

    fun setCategoryFilter(category: String?) {
        _selectedCategoryFilter.value = category
        performSearch()
    }

    fun toggleFilters() {
        _showFilters.value = !_showFilters.value
    }

    fun clearFilters() {
        _selectedStatusFilter.value = null
        _selectedTypeFilter.value = null
        _selectedCategoryFilter.value = null
        performSearch()
    }

    fun performSearch() {
        val query = _searchQuery.value
        if (query.isEmpty()) {
            _searchResults.value = UiState.Success(emptyList())
            return
        }
        
        _searchResults.value = UiState.Loading
        // In a real app, we would call the repository here with query and filters.
        // For now, simulating an empty result.
        viewModelScope.launch {
            delay(500)
            _searchResults.value = UiState.Success(emptyList())
        }
    }
}
