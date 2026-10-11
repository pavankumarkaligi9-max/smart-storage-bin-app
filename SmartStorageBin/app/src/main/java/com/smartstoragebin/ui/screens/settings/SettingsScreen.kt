package com.smartstoragebin.ui.screens.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    onBackClick: () -> Unit
) {
    val state by viewModel.settingsState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            
            // Notifications
            Text(
                text = "Notifications",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp)
            )
            ListItem(
                headlineContent = { Text("Enable Notifications") },
                supportingContent = { Text("Get alerted about your files") },
                leadingContent = { Icon(Icons.Default.Notifications, contentDescription = null) },
                trailingContent = {
                    Switch(checked = state.notificationsEnabled, onCheckedChange = viewModel::setNotificationsEnabled)
                }
            )
            ListItem(
                headlineContent = { Text("Expiry Warnings") },
                supportingContent = { Text("Warn before files expire") },
                leadingContent = { Icon(Icons.Default.Warning, contentDescription = null) },
                trailingContent = {
                    Switch(checked = state.expiryWarningsEnabled, onCheckedChange = viewModel::setExpiryWarningsEnabled, enabled = state.notificationsEnabled)
                }
            )
            
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            
            // File Management
            Text(
                text = "File Management",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 8.dp)
            )
            ListItem(
                headlineContent = { Text("Default Temporary Duration") },
                supportingContent = { Text("${state.defaultTemporaryDurationDays} Days") },
                leadingContent = { Icon(Icons.Default.Timer, contentDescription = null) }
            )
            ListItem(
                headlineContent = { Text("Expiry Warning Period") },
                supportingContent = { Text("${state.expiryWarningPeriodDays} Days before expiry") },
                leadingContent = { Icon(Icons.Default.Schedule, contentDescription = null) }
            )
            ListItem(
                headlineContent = { Text("Duplicate Detection") },
                supportingContent = { Text("Find and remove duplicates") },
                leadingContent = { Icon(Icons.Default.FileCopy, contentDescription = null) },
                trailingContent = {
                    Switch(checked = state.duplicateDetectionEnabled, onCheckedChange = viewModel::setDuplicateDetectionEnabled)
                }
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            // Recycle Bin
            Text(
                text = "Recycle Bin",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 8.dp)
            )
            ListItem(
                headlineContent = { Text("Auto-delete after 30 days") },
                supportingContent = { Text("Permanently delete bin items") },
                leadingContent = { Icon(Icons.Default.AutoDelete, contentDescription = null) },
                trailingContent = {
                    Switch(checked = state.autoDeleteBinEnabled, onCheckedChange = viewModel::setAutoDeleteBinEnabled)
                }
            )
            ListItem(
                headlineContent = { Text("Empty Recycle Bin") },
                supportingContent = { Text("Permanently delete all items") },
                leadingContent = { Icon(Icons.Default.DeleteForever, contentDescription = null) },
                colors = ListItemDefaults.colors(
                    headlineColor = MaterialTheme.colorScheme.error,
                    leadingIconColor = MaterialTheme.colorScheme.error
                )
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            
            // Appearance
            Text(
                text = "Appearance",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 8.dp)
            )
            ListItem(
                headlineContent = { Text("Theme") },
                supportingContent = { Text(state.theme) },
                leadingContent = { Icon(Icons.Default.Palette, contentDescription = null) }
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            
            // Storage
            Text(
                text = "Storage",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 8.dp)
            )
            ListItem(
                headlineContent = { Text("File Access Permissions") },
                supportingContent = { Text("Manage storage access") },
                leadingContent = { Icon(Icons.Default.Folder, contentDescription = null) }
            )
            ListItem(
                headlineContent = { Text("Clear Cache") },
                supportingContent = { Text("24.5 MB used") },
                leadingContent = { Icon(Icons.Default.CleaningServices, contentDescription = null) }
            )
            
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            
            // About
            Text(
                text = "About",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 8.dp)
            )
            ListItem(
                headlineContent = { Text("App Version") },
                supportingContent = { Text("1.0.0") },
                leadingContent = { Icon(Icons.Default.Info, contentDescription = null) }
            )
            ListItem(
                headlineContent = { Text("Developer") },
                supportingContent = { Text("Smart Storage Bin Team") },
                leadingContent = { Icon(Icons.Default.Person, contentDescription = null) }
            )
            ListItem(
                headlineContent = { Text("License") },
                supportingContent = { Text("Apache 2.0") },
                leadingContent = { Icon(Icons.Default.Gavel, contentDescription = null) }
            )
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
