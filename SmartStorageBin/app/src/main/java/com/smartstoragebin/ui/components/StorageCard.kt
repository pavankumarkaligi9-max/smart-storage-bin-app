package com.smartstoragebin.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.smartstoragebin.data.model.StorageInfo

@Composable
fun StorageCard(
    storageInfo: StorageInfo,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Storage Overview",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            
            val progress = if (storageInfo.totalStorage > 0) {
                storageInfo.usedStorage.toFloat() / storageInfo.totalStorage.toFloat()
            } else 0f
            
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Used: ${storageInfo.usedStorage / (1024 * 1024)} MB",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "Free: ${storageInfo.freeStorage / (1024 * 1024)} MB",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "Total: ${storageInfo.totalStorage / (1024 * 1024)} MB",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
