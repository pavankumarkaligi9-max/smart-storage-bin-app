package com.smartstoragebin.ui.screens.files

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.smartstoragebin.data.model.FileStatus
import com.smartstoragebin.ui.screens.UiState
import com.smartstoragebin.ui.components.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FileDetailScreen(
    fileId: Long,
    viewModel: FileDetailViewModel = viewModel(),
    onBackClick: () -> Unit
) {
    val fileDetailState by viewModel.fileDetail.collectAsState()
    val showDeleteDialog by viewModel.showDeleteDialog.collectAsState()
    val showRenameDialog by viewModel.showRenameDialog.collectAsState()
    val actionResult by viewModel.actionResult.collectAsState(initial = null)
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(fileId) {
        viewModel.loadFile(fileId)
    }

    LaunchedEffect(actionResult) {
        actionResult?.let {
            snackbarHostState.showSnackbar(it)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("File Details") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        when (val state = fileDetailState) {
            is UiState.Loading -> FullScreenLoading()
            is UiState.Error -> ErrorState(message = state.message, onRetry = { viewModel.loadFile(fileId) })
            is UiState.Success -> {
                val file = state.data
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.InsertDriveFile,
                        contentDescription = "File Icon",
                        modifier = Modifier.size(100.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    
                    Text(text = file.name, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)

                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            DetailRow("Type", file.type)
                            DetailRow("Size", "${file.size} bytes")
                            DetailRow("Path", file.path)
                            DetailRow("Category", file.category)
                            DetailRow("Created At", file.createdAt)
                            DetailRow("Status", file.status.name)
                            
                            if (file.status == FileStatus.TEMPORARY) {
                                Divider(modifier = Modifier.padding(vertical = 8.dp))
                                DetailRow("Expiry Date", file.expiryDate ?: "Unknown")
                                // Example mock badge since we can't compile extensions here
                                StatusBadge(text = "Expiring Soon") 
                            }
                            
                            Divider(modifier = Modifier.padding(vertical = 8.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("Hash: ${file.hash.take(10)}...", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(1f))
                                IconButton(onClick = { /* Copy to clipboard */ }) {
                                    Icon(Icons.Default.ContentCopy, contentDescription = "Copy Hash")
                                }
                            }
                        }
                    }

                    // Action Buttons Grid
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Actions", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                                ActionIconButton(Icons.Default.OpenInNew, "Open") { viewModel.openFile() }
                                ActionIconButton(Icons.Default.Share, "Share") { viewModel.shareFile() }
                                ActionIconButton(Icons.Default.Edit, "Rename") { viewModel.showRenameDialog() }
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                                ActionIconButton(Icons.Default.DriveFileMove, "Move") { viewModel.moveFile() }
                                ActionIconButton(Icons.Default.ContentCopy, "Copy") { viewModel.copyFile() }
                                ActionIconButton(Icons.Default.Delete, "Delete", MaterialTheme.colorScheme.error) { viewModel.showDeleteDialog() }
                            }
                        }
                    }

                    if (file.status == FileStatus.TEMPORARY) {
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("Temporary Options", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(8.dp))
                                Button(onClick = { viewModel.extendFile("new_date") }, modifier = Modifier.fillMaxWidth()) {
                                    Text("Extend Expiry")
                                }
                                OutlinedButton(onClick = { viewModel.convertToPermanent() }, modifier = Modifier.fillMaxWidth()) {
                                    Text("Keep Permanently")
                                }
                            }
                        }
                    }
                }
            }
        }

        if (showDeleteDialog) {
            ConfirmationDialog(
                title = "Delete File",
                message = "Are you sure you want to delete this file?",
                onConfirm = { viewModel.deleteFile() },
                onDismiss = { viewModel.dismissDeleteDialog() }
            )
        }
        
        if (showRenameDialog) {
             // Mocking Rename dialog
             ConfirmationDialog(
                title = "Rename File",
                message = "Rename feature not fully implemented in UI layer yet.",
                onConfirm = { viewModel.renameFile("NewName.ext") },
                onDismiss = { viewModel.dismissRenameDialog() }
            )
        }
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun ActionIconButton(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, tint: androidx.compose.ui.graphics.Color = LocalContentColor.current, onClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        IconButton(onClick = onClick) {
            Icon(icon, contentDescription = label, tint = tint)
        }
        Text(label, style = MaterialTheme.typography.labelSmall)
    }
}
