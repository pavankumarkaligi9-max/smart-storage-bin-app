package com.smartstoragebin.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.smartstoragebin.data.model.FileItem

@Composable
fun DuplicateDialog(
    newFile: FileItem,
    existingFile: FileItem,
    onKeepExisting: () -> Unit,
    onReplace: () -> Unit,
    onKeepBoth: () -> Unit,
    onCancel: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onCancel,
        title = { Text("Duplicate File Detected") },
        text = {
            Column {
                Text("A file with the same content already exists.", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(16.dp))
                
                Text("Existing File:", fontWeight = FontWeight.Bold)
                Text(existingFile.name)
                Text(existingFile.formattedSize())
                Text(existingFile.path)
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text("New File:", fontWeight = FontWeight.Bold)
                Text(newFile.name)
                Text(newFile.formattedSize())
                Text(newFile.path)
            }
        },
        confirmButton = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Button(onClick = onReplace, modifier = Modifier.fillMaxWidth()) {
                    Text("Replace Existing")
                }
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = onKeepBoth, modifier = Modifier.fillMaxWidth()) {
                    Text("Keep Both")
                }
            }
        },
        dismissButton = {
            Column(modifier = Modifier.fillMaxWidth()) {
                OutlinedButton(onClick = onKeepExisting, modifier = Modifier.fillMaxWidth()) {
                    Text("Keep Existing (Discard New)")
                }
                Spacer(modifier = Modifier.height(8.dp))
                TextButton(onClick = onCancel, modifier = Modifier.fillMaxWidth()) {
                    Text("Cancel")
                }
            }
        }
    )
}
