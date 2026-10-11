package com.smartstoragebin.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.smartstoragebin.data.model.TemporarySummary
import com.smartstoragebin.ui.theme.LocalCustomColors

@Composable
fun TemporaryFileSummaryCard(
    summary: TemporarySummary,
    onViewAllClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Temporary Files",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                SummaryItem("Total", summary.totalFiles.toString(), MaterialTheme.colorScheme.primary)
                SummaryItem("Expired", summary.expiredCount.toString(), LocalCustomColors.current.statusExpired)
                SummaryItem("Expiring Today", summary.expiringTodayCount.toString(), LocalCustomColors.current.statusExpiring)
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onViewAllClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("View All")
            }
        }
    }
}

@Composable
private fun SummaryItem(label: String, value: String, color: androidx.compose.ui.graphics.Color) {
    Column(horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally) {
        Text(text = value, style = MaterialTheme.typography.titleLarge, color = color, fontWeight = FontWeight.Bold)
        Text(text = label, style = MaterialTheme.typography.bodySmall)
    }
}
