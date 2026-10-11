package com.smartstoragebin.ui.screens.temporary

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.smartstoragebin.data.model.*
import com.smartstoragebin.ui.screens.UiState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TemporaryScreen(
    onFileClick: (Long) -> Unit,
    onBackClick: () -> Unit,
    viewModel: TemporaryViewModel = viewModel()
) {
    val temporaryFilesState by viewModel.temporaryFiles.collectAsState()
    val temporarySummaryState by viewModel.temporarySummary.collectAsState()
    val selectedFilter by viewModel.selectedFilter.collectAsState()
    val showExpiredDialog by viewModel.showExpiredDialog.collectAsState()
    val showExtendDialog by viewModel.showExtendDialog.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.actionResult.collect { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Temporary Files") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { /* TODO: Show filter menu */ }) {
                        Icon(Icons.Default.FilterList, contentDescription = "Filter")
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextButton(onClick = { /* Review Expired */ }) {
                        Text("Review Expired")
                    }
                    Button(onClick = { /* Extend All */ }) {
                        Text("Extend All Expiring")
                    }
                }
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {
            // Summary Section
            if (temporarySummaryState is UiState.Success) {
                val summary = (temporarySummaryState as UiState.Success<TemporarySummary>).data
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column {
                        Text("Total", style = MaterialTheme.typography.labelMedium)
                        Text("${summary.totalFiles}", style = MaterialTheme.typography.titleMedium)
                    }
                    Column {
                        Text("Expired", style = MaterialTheme.typography.labelMedium)
                        Text("${summary.expiredCount}", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.error)
                    }
                    Column {
                        Text("Expiring", style = MaterialTheme.typography.labelMedium)
                        Text("${summary.expiringSoonCount}", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.tertiary)
                    }
                }
            }

            // Filter Chips
            ScrollableTabRow(
                selectedTabIndex = selectedFilter.ordinal,
                edgePadding = 16.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                TemporaryFilter.values().forEachIndexed { index, filter ->
                    Tab(
                        selected = selectedFilter == filter,
                        onClick = { viewModel.setFilter(filter) },
                        text = { Text(filter.name.lowercase().capitalize()) }
                    )
                }
            }

            // File List
            when (temporaryFilesState) {
                is UiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is UiState.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                        Text("Error: ${(temporaryFilesState as UiState.Error).message}")
                    }
                }
                is UiState.Success -> {
                    val files = (temporaryFilesState as UiState.Success<List<FileItem>>).data
                    if (files.isEmpty()) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                            Text("No Temporary Files")
                        }
                    } else {
                        LazyColumn(modifier = Modifier.fillMaxSize()) {
                            items(files) { file ->
                                Card(
                                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                                    onClick = { onFileClick(file.id) }
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Text(file.name, style = MaterialTheme.typography.titleMedium)
                                        Text("Size: ${file.size} bytes", style = MaterialTheme.typography.bodyMedium)
                                        Text("Expires: ${file.expiryDate}", style = MaterialTheme.typography.bodySmall)
                                        
                                        Row(
                                            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                                            horizontalArrangement = Arrangement.End
                                        ) {
                                            TextButton(onClick = { viewModel.showExtendDialogFor(file) }) {
                                                Text("Extend")
                                            }
                                            TextButton(onClick = { viewModel.convertToPermanent(file.id) }) {
                                                Text("Keep")
                                            }
                                            TextButton(onClick = { viewModel.deleteFile(file.id) }, colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)) {
                                                Text("Delete")
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

        // Dialogs
        showExpiredDialog?.let { file ->
            AlertDialog(
                onDismissRequest = { viewModel.dismissDialogs() },
                title = { Text("Expired File") },
                text = { Text("The file ${file.name} has expired.") },
                confirmButton = {
                    TextButton(onClick = { 
                        viewModel.deleteFile(file.id)
                        viewModel.dismissDialogs() 
                    }) {
                        Text("Delete")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { viewModel.dismissDialogs() }) {
                        Text("Cancel")
                    }
                }
            )
        }

        showExtendDialog?.let { file ->
            AlertDialog(
                onDismissRequest = { viewModel.dismissDialogs() },
                title = { Text("Extend Expiry") },
                text = { Text("Extend expiry for ${file.name} by 7 days?") },
                confirmButton = {
                    TextButton(onClick = { 
                        viewModel.extendFile(file.id, "7_days_later")
                        viewModel.dismissDialogs() 
                    }) {
                        Text("Extend")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { viewModel.dismissDialogs() }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}
