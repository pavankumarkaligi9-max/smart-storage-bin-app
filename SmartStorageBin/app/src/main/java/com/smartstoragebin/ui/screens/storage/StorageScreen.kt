package com.smartstoragebin.ui.screens.storage

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.smartstoragebin.data.model.Category
import com.smartstoragebin.data.model.StorageInfo
import com.smartstoragebin.ui.screens.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StorageScreen(
    viewModel: StorageViewModel = viewModel()
) {
    val storageInfoState by viewModel.storageInfo.collectAsState()
    val categoriesState by viewModel.categories.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Storage Analyzer") })
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {
            when (storageInfoState) {
                is UiState.Loading -> CircularProgressIndicator(modifier = Modifier.align(androidx.compose.ui.Alignment.CenterHorizontally))
                is UiState.Error -> Text("Error loading storage info")
                is UiState.Success -> {
                    val info = (storageInfoState as UiState.Success<StorageInfo>).data
                    Card(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Storage Overview", style = MaterialTheme.typography.titleLarge)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Total: ${info.totalStorage} bytes")
                            Text("Used: ${info.usedStorage} bytes")
                            Text("Free: ${info.freeStorage} bytes")
                        }
                    }

                    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                        Card(modifier = Modifier.weight(1f).padding(end = 8.dp)) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("Temporary")
                                Text("${info.temporaryStorage} bytes")
                            }
                        }
                        Card(modifier = Modifier.weight(1f).padding(start = 8.dp)) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("Recycle Bin")
                                Text("${info.binStorage} bytes")
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text("Categories", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(horizontal = 16.dp))

            when (categoriesState) {
                is UiState.Loading -> CircularProgressIndicator()
                is UiState.Error -> Text("Error loading categories")
                is UiState.Success -> {
                    val categories = (categoriesState as UiState.Success<List<Category>>).data
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(16.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        items(categories) { category ->
                            Card(modifier = Modifier.padding(8.dp).fillMaxWidth()) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(category.name, style = MaterialTheme.typography.titleMedium)
                                    Text("${category.fileCount} files | ${category.totalSize} bytes", style = MaterialTheme.typography.bodySmall)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
