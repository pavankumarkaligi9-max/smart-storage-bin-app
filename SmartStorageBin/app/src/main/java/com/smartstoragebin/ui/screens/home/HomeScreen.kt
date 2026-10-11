package com.smartstoragebin.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.smartstoragebin.ui.screens.UiState
import com.smartstoragebin.ui.components.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(),
    onNavigateToFiles: () -> Unit,
    onNavigateToTemporary: () -> Unit,
    onNavigateToCategories: () -> Unit,
    onNavigateToStorage: () -> Unit,
    onNavigateToBin: () -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onFileClick: (Long) -> Unit
) {
    val storageInfo by viewModel.storageInfo.collectAsState()
    val temporarySummary by viewModel.temporarySummary.collectAsState()
    val categories by viewModel.categories.collectAsState()
    val recentFiles by viewModel.recentFiles.collectAsState()
    val duplicates by viewModel.duplicates.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadDashboardData()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Smart File Manager") },
                actions = {
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Storage Card
            when (val state = storageInfo) {
                is UiState.Loading -> ShimmerLoadingCard()
                is UiState.Success -> {
                    Card(onClick = onNavigateToStorage, modifier = Modifier.fillMaxWidth()) {
                        StorageCard(storageInfo = state.data)
                    }
                }
                is UiState.Error -> ErrorState(message = state.message, onRetry = { viewModel.loadDashboardData() })
            }

            // Quick Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                QuickActionButton(icon = Icons.Outlined.Folder, label = "All Files", onClick = onNavigateToFiles, modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.width(8.dp))
                QuickActionButton(icon = Icons.Outlined.Schedule, label = "Temp Files", onClick = onNavigateToTemporary, modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.width(8.dp))
                QuickActionButton(icon = Icons.Outlined.Search, label = "Search", onClick = onNavigateToSearch, modifier = Modifier.weight(1f))
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                QuickActionButton(icon = Icons.Outlined.Category, label = "Categories", onClick = onNavigateToCategories, modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.width(8.dp))
                QuickActionButton(icon = Icons.Outlined.PieChart, label = "Analyzer", onClick = onNavigateToStorage, modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.width(8.dp))
                QuickActionButton(icon = Icons.Outlined.Delete, label = "Recycle Bin", onClick = onNavigateToBin, modifier = Modifier.weight(1f))
            }

            // Temporary Summary
            when (val state = temporarySummary) {
                is UiState.Loading -> ShimmerLoadingCard()
                is UiState.Success -> TemporaryFileSummaryCard(summary = state.data, onViewAllClick = onNavigateToTemporary)
                is UiState.Error -> {}
            }

            // Duplicates
            when (val state = duplicates) {
                is UiState.Loading -> ShimmerLoadingCard()
                is UiState.Success -> {
                    if (state.data.isNotEmpty()) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = onNavigateToStorage
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("Duplicate Files Found", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("${state.data.size} groups of duplicates wasting space.", style = MaterialTheme.typography.bodyMedium)
                                Button(onClick = onNavigateToStorage, modifier = Modifier.align(Alignment.End)) {
                                    Text("Review")
                                }
                            }
                        }
                    }
                }
                is UiState.Error -> {}
            }

            // Categories
            Text("Categories", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            when (val state = categories) {
                is UiState.Loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                is UiState.Success -> {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(state.data) { category ->
                            CategoryCard(category = category, onClick = { onNavigateToCategories() })
                        }
                    }
                }
                is UiState.Error -> Text("Failed to load categories", color = MaterialTheme.colorScheme.error)
            }

            // Recent Files
            Text("Recent Files", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            when (val state = recentFiles) {
                is UiState.Loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                is UiState.Success -> {
                    if (state.data.isEmpty()) {
                        EmptyState(icon = Icons.Outlined.FolderOpen, title = "No Recent Files", subtitle = "Your recently accessed files will appear here")
                    } else {
                        state.data.forEach { file ->
                            FileCard(file = file, onClick = { onFileClick(file.id) })
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
                is UiState.Error -> Text("Failed to load recent files", color = MaterialTheme.colorScheme.error)
            }
        }
    }
}
