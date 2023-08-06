package com.example.samplelockscreenwidget.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.samplelockscreenwidget.AppAlarmManager

class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            // Reschedule alarm after boot
            AppAlarmManager.scheduleLockscreenWidgets(context)
        }
    }
}