package com.smartstoragebin.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.smartstoragebin.data.model.FileItem

@Composable
fun ExpiryWarningDialog(
    file: FileItem,
    onExtend: () -> Unit,
    onKeepPermanent: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("File Expiring Soon") },
        text = {
            Column {
                Text(file.name)
                Spacer(modifier = Modifier.height(8.dp))
                Text("This file will expire in ${file.remainingDays()} days. Would you like to extend it or keep it permanently?")
            }
        },
        confirmButton = {
            Button(onClick = onExtend) {
                Text("Extend")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onKeepPermanent) {
                Text("Keep Permanent")
            }
        }
    )
}

@Composable
fun ExpiredFileDialog(
    file: FileItem,
    onDelete: () -> Unit,
    onKeepPermanent: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Temporary File Expired") },
        text = {
            Column {
                Text(file.name)
                Spacer(modifier = Modifier.height(8.dp))
                Text("This file has expired. Delete it to free up space?")
            }
        },
        confirmButton = {
            Button(
                onClick = onDelete,
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                    containerColor = androidx.compose.material3.MaterialTheme.colorScheme.error
                )
            ) {
                Text("Delete")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onKeepPermanent) {
                Text("Keep Permanent")
            }
        }
    )
}
