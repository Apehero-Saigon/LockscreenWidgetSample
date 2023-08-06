package com.example.samplelockscreenwidget

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.os.Build
import android.util.Log
import com.example.samplelockscreenwidget.receiver.AlarmReceiver

object AppAlarmManager {
    private const val TAG = "AppAlarmManager"
    private const val lockscreenWidgetRequestCode = 10000

    fun scheduleLockscreenWidgets(context: Context) {
        scheduleLockscreenWidget(context, lockscreenWidgetRequestCode, 6)
    }

    fun cancelLockscreenWidgets(context: Context) {
        // Create a pending intent with the same intent and request code
        val pendingIntent = PendingIntent.getBroadcast(
            context, lockscreenWidgetRequestCode,
            Intent(context, AlarmReceiver::class.java), PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_NO_CREATE
        )
        // Cancel it with alarm manager
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
    }

    private fun scheduleLockscreenWidget(context: Context, requestCode: Int, atHour: Int, atMinute: Int = 0) {
        Log.d(TAG, "scheduleLockscreenWidget")
        val alarmIntent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context, requestCode, alarmIntent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        // Set time to show widget
        val currentTimeMillis = System.currentTimeMillis()
        val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = currentTimeMillis
            set(Calendar.HOUR_OF_DAY, atHour)
            set(Calendar.MINUTE, atMinute)
            set(Calendar.SECOND, 0)

            // If current time is after set time, push set time back one day
            if (timeInMillis <= currentTimeMillis) add(Calendar.HOUR_OF_DAY, 24)
        }

        // Schedule alarm
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)

        Log.d(TAG, "scheduleFullscreenReminder: alarm scheduled at ${calendar.time}")
    }
}