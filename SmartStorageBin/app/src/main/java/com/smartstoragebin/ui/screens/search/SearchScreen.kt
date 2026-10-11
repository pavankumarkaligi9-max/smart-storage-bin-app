package com.smartstoragebin.ui.screens.search

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.unit.dp
import com.smartstoragebin.ui.screens.UiState
import com.smartstoragebin.ui.components.FileCard
import com.smartstoragebin.ui.components.LoadingState
import com.smartstoragebin.ui.components.ErrorState
import com.smartstoragebin.ui.components.EmptyState
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    onFileClick: (Long) -> Unit,
    onBackClick: () -> Unit
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()
    val showFilters by viewModel.showFilters.collectAsState()
    val selectedStatus by viewModel.selectedStatusFilter.collectAsState()
    val selectedType by viewModel.selectedTypeFilter.collectAsState()

    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        delay(100)
        focusRequester.requestFocus()
    }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        SearchBar(
                            query = searchQuery,
                            onQueryChange = viewModel::setSearchQuery,
                            onSearch = { viewModel.performSearch() },
                            active = false,
                            onActiveChange = {},
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(end = 16.dp)
                                .focusRequester(focusRequester),
                            placeholder = { Text("Search files...") },
                            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) }
                        ) {}
                    },
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    }
                )
                
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextButton(onClick = { viewModel.toggleFilters() }) {
                        Text(if (showFilters) "Hide Filters" else "Show Filters")
                    }
                    if (showFilters) {
                        TextButton(onClick = { viewModel.clearFilters() }) {
                            Text("Clear Filters")
                        }
                    }
                }
                
                if (showFilters) {
                    // Filters section
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = selectedStatus == "Temporary",
                            onClick = { viewModel.setStatusFilter(if (selectedStatus == "Temporary") null else "Temporary") },
                            label = { Text("Temporary") }
                        )
                        FilterChip(
                            selected = selectedStatus == "Permanent",
                            onClick = { viewModel.setStatusFilter(if (selectedStatus == "Permanent") null else "Permanent") },
                            label = { Text("Permanent") }
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = selectedType == "Documents",
                            onClick = { viewModel.setTypeFilter(if (selectedType == "Documents") null else "Documents") },
                            label = { Text("Documents") }
                        )
                        FilterChip(
                            selected = selectedType == "Images",
                            onClick = { viewModel.setTypeFilter(if (selectedType == "Images") null else "Images") },
                            label = { Text("Images") }
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
            if (searchQuery.isEmpty()) {
                EmptyState(message = "Search your files", icon = Icons.Default.Search)
            } else {
                when (val state = searchResults) {
                    is UiState.Loading -> LoadingState()
                    is UiState.Error -> ErrorState(message = state.message)
                    is UiState.Success -> {
                        if (state.data.isEmpty()) {
                            EmptyState(message = "No files found")
                        } else {
                            Column {
                                Text(
                                    text = "${state.data.size} results found",
                                    style = MaterialTheme.typography.labelMedium,
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                                )
                                LazyColumn(
                                    contentPadding = PaddingValues(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    items(state.data) { file ->
                                        FileCard(file = file, onClick = { onFileClick(file.id) })
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
