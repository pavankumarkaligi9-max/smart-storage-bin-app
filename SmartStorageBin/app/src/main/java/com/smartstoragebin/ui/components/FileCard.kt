package com.smartstoragebin.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.InsertDriveFile
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.smartstoragebin.data.model.ExpiryStatus
import com.smartstoragebin.data.model.FileItem
import com.smartstoragebin.data.model.FileStatus
import com.smartstoragebin.ui.theme.LocalCustomColors

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FileCard(
    file: FileItem,
    onClick: () -> Unit,
    onLongClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        shape = MaterialTheme.shapes.medium,
        modifier = modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.InsertDriveFile,
                contentDescription = null,
                modifier = Modifier.size(40.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = file.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "${file.formattedSize()} • ${file.createdAt}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            if (file.status == FileStatus.TEMPORARY) {
                Spacer(modifier = Modifier.width(8.dp))
                val customColors = LocalCustomColors.current
                val color = when (file.expiryStatus()) {
                    ExpiryStatus.ACTIVE -> customColors.statusActive
                    ExpiryStatus.EXPIRING_SOON -> customColors.statusExpiring
                    ExpiryStatus.EXPIRED -> customColors.statusExpired
                }
                StatusBadge(
                    text = "${file.remainingDays()}d",
                    backgroundColor = color
                )
            }
        }
    }
}
