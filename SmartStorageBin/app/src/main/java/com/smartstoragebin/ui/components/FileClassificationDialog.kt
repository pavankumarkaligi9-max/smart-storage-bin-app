package com.smartstoragebin.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.smartstoragebin.data.model.FileItem

@Composable
fun FileClassificationDialog(
    file: FileItem,
    onTemporarySelected: (days: Int) -> Unit,
    onPermanentSelected: () -> Unit,
    onDismiss: () -> Unit
) {
    var showRetentionOptions by remember { mutableStateOf(false) }
    var selectedDays by remember { mutableStateOf(7) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("New File Detected") },
        text = {
            Column {
                Text("Name: ${file.name}")
                Text("Size: ${file.formattedSize()}")
                Spacer(modifier = Modifier.height(16.dp))
                if (!showRetentionOptions) {
                    Text("How would you like to manage this file?")
                } else {
                    Text("Select retention period:")
                    val options = listOf(1, 3, 7, 15, 30)
                    options.forEach { days ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = selectedDays == days,
                                onClick = { selectedDays = days }
                            )
                            Text("$days Days")
                        }
                    }
                }
            }
        },
        confirmButton = {
            if (!showRetentionOptions) {
                Button(onClick = { showRetentionOptions = true }) {
                    Text("Temporary")
                }
            } else {
                Button(onClick = { onTemporarySelected(selectedDays) }) {
                    Text("Confirm")
                }
            }
        },
        dismissButton = {
            if (!showRetentionOptions) {
                OutlinedButton(onClick = onPermanentSelected) {
                    Text("Permanent")
                }
            } else {
                TextButton(onClick = { showRetentionOptions = false }) {
                    Text("Back")
                }
            }
        }
    )
}
