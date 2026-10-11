package com.smartstoragebin.ui.screens.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smartstoragebin.data.model.Category
import com.smartstoragebin.ui.screens.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CategoriesViewModel : ViewModel() {

    private val _categories = MutableStateFlow<UiState<List<Category>>>(UiState.Loading)
    val categories: StateFlow<UiState<List<Category>>> = _categories.asStateFlow()

    init {
        loadCategories()
    }

    fun loadCategories() {
        viewModelScope.launch {
            _categories.value = UiState.Loading
            val cats = listOf(
                Category("Documents", 25, 3200000L, "icon_doc"),
                Category("Images", 150, 45000000L, "icon_img"),
                Category("Videos", 12, 120000000L, "icon_vid"),
                Category("Audio", 45, 15000000L, "icon_audio"),
                Category("Archives", 5, 5000000L, "icon_archive"),
                Category("Apps", 8, 80000000L, "icon_app"),
                Category("Other", 30, 20000000L, "icon_other")
            )
            _categories.value = UiState.Success(cats)
        }
    }
}
