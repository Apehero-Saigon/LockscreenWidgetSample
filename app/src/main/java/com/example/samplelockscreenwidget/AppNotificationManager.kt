package com.example.samplelockscreenwidget

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

object AppNotificationManager {
    const val DefaultNotificationChannelId = "default"
    const val DefaultNotificationChannelName = "Default"
    const val DefaultNotificationChannelDesc = "Default notification channel"

    const val LockscreenWidgetNotificationId = 20000

    fun createNotificationChannels(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel.
            val name = DefaultNotificationChannelName
            val descriptionText = DefaultNotificationChannelDesc
            val importance = NotificationManager.IMPORTANCE_HIGH
            val mChannel = NotificationChannel(DefaultNotificationChannelId, name, importance)
            mChannel.description = descriptionText
            // Register the channel with the system. You can't change the importance
            // or other notification behaviors after this.
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }
    }

    fun sendNotification(context: Context, notificationId: Int, notification: Notification) {
        with(NotificationManagerCompat.from(context)) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                return
            }

            notify(notificationId, notification)
        }
    }

    fun cancelNotification(context: Context, notificationId: Int) {
        with(NotificationManagerCompat.from(context)) {
            cancel(notificationId)
        }
    }

    fun sendLockscreenWidgetNotification(context: Context) {
        val fullScreenIntent = Intent(context, LockscreenWidgetActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        val fullScreenPendingIntent = PendingIntent.getActivity(
            context, 0,
            fullScreenIntent, PendingIntent.FLAG_IMMUTABLE
        )

        val notificationBuilder =
            NotificationCompat.Builder(context, DefaultNotificationChannelId)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(context.getString(R.string.app_name))
                .setStyle(NotificationCompat.DecoratedCustomViewStyle())
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_CALL)
                .setFullScreenIntent(fullScreenPendingIntent, true)

        // Cancel old notification
        cancelNotification(context, LockscreenWidgetNotificationId)
        // Send new notification
        sendNotification(context, LockscreenWidgetNotificationId, notificationBuilder.build())
    }
}