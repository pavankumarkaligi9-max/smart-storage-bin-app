package com.smartstoragebin.ui.screens.files

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.smartstoragebin.ui.screens.UiState
import com.smartstoragebin.ui.components.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilesScreen(
    viewModel: FilesViewModel = viewModel(),
    onFileClick: (Long) -> Unit,
    onBackClick: () -> Unit
) {
    val filesState by viewModel.files.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val sortOrder by viewModel.sortOrder.collectAsState()
    val viewMode by viewModel.viewMode.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val showDialog by viewModel.showClassificationDialog.collectAsState()

    var showSortMenu by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadFiles()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("All Files") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.toggleViewMode() }) {
                        Icon(
                            if (viewMode == ViewMode.LIST) Icons.Default.GridView else Icons.Default.ViewList,
                            contentDescription = "Toggle View"
                        )
                    }
                    Box {
                        IconButton(onClick = { showSortMenu = true }) {
                            Icon(Icons.Default.Sort, contentDescription = "Sort")
                        }
                        DropdownMenu(
                            expanded = showSortMenu,
                            onDismissRequest = { showSortMenu = false }
                        ) {
                            SortOrder.values().forEach { order ->
                                DropdownMenuItem(
                                    text = { Text(order.name) },
                                    onClick = {
                                        viewModel.setSortOrder(order)
                                        showSortMenu = false
                                    }
                                )
                            }
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { viewModel.showNewFileDialog() }) {
                Icon(Icons.Default.Add, contentDescription = "Add File")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            SearchBar(
                query = searchQuery,
                onQueryChange = { viewModel.setSearchQuery(it) },
                modifier = Modifier.padding(16.dp)
            )

            // Filter Chips
            val categories = listOf("All", "Documents", "Images", "Videos", "Audio", "Archives")
            androidx.compose.foundation.lazy.LazyRow(
                modifier = Modifier.padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories) { category ->
                    FilterChip(
                        selected = (selectedCategory == category) || (selectedCategory == null && category == "All"),
                        onClick = { viewModel.filterByCategory(if (category == "All") null else category) },
                        label = { Text(category) }
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))

            when (val state = filesState) {
                is UiState.Loading -> FullScreenLoading()
                is UiState.Error -> ErrorState(message = state.message, onRetry = { viewModel.loadFiles() })
                is UiState.Success -> {
                    if (state.data.isEmpty()) {
                        EmptyState(message = "No files found")
                    } else {
                        if (viewMode == ViewMode.LIST) {
                            LazyColumn(
                                contentPadding = PaddingValues(16.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(state.data) { file ->
                                    FileCard(file = file, onClick = { onFileClick(file.id) })
                                }
                            }
                        } else {
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(2),
                                contentPadding = PaddingValues(16.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(state.data) { file ->
                                    FileCard(file = file, onClick = { onFileClick(file.id) }) // Assume FileCard adapts to grid or use GridFileCard
                                }
                            }
                        }
                    }
                }
            }
        }

        if (showDialog) {
            FileClassificationDialog(
                onDismiss = { viewModel.dismissNewFileDialog() },
                onClassify = { status, expiry ->
                    // For demo purposes, pretend we upload and classify a file id=0
                    viewModel.classifyFile(0L, status, expiry)
                    viewModel.dismissNewFileDialog()
                }
            )
        }
    }
}
