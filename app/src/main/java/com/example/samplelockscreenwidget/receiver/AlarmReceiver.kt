package com.example.samplelockscreenwidget.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.samplelockscreenwidget.AppNotificationManager

class AlarmReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        AppNotificationManager.sendLockscreenWidgetNotification(context)
    }
}