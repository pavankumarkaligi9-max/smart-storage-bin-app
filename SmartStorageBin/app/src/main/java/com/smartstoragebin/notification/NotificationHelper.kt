package com.smartstoragebin.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.smartstoragebin.MainActivity

object NotificationHelper {

    const val CHANNEL_ID = "smart_storage_bin_file_expiry"
    const val CHANNEL_NAME = "Temporary File Expiry Alerts"
    const val NOTIFICATION_ID_BASE = 1000

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
                description = "Notifies when temporary files have reached their expiration date"
                enableVibration(true)
            }
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun showFileExpiredNotification(
        context: Context,
        fileId: Long,
        fileName: String,
        fileSizeFormatted: String
    ) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("EXTRA_NAVIGATE_TO", "temporary")
            putExtra("EXTRA_FILE_ID", fileId)
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            fileId.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setContentTitle("⏰ Retention Period Expired: $fileName")
            .setContentText("File ($fileSizeFormatted) reached expiration. Click to Keep Permanent or Extend +7 Days.")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("The retention duration for \"$fileName\" ($fileSizeFormatted) is now completed. Under the Zero Auto-Deletion policy, please choose an action: Convert to Permanent, Extend (+7 Days), or Move to Bin.")
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify((NOTIFICATION_ID_BASE + fileId).toInt(), notification)
    }
}
