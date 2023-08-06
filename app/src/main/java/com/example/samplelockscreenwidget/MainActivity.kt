package com.example.samplelockscreenwidget

import android.Manifest
import android.content.ComponentName
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.example.samplelockscreenwidget.receiver.BootReceiver
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppNotificationManager.createNotificationChannels(this)

        setContent {
            MainActivityContent(
                onShowDelayed = {
                    scheduleLockscreenWidgetNotification()
                    Toast.makeText(this, "Scheduled in 5 seconds", Toast.LENGTH_SHORT).show()
                },
                onSchedule = {
                    scheduleRepeatingLockscreenWidget()
                    Toast.makeText(this, "Scheduled repeating at 6 A.M.", Toast.LENGTH_SHORT).show()
                },
                onCancelSchedule = {
                    cancelRepeatingLockscreenWidget()
                    Toast.makeText(this, "Canceled repeating", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }

    private fun scheduleLockscreenWidgetNotification() {
        lifecycleScope.launch {
            delay(5000)
            AppNotificationManager.sendLockscreenWidgetNotification(this@MainActivity)
        }
    }

    private fun scheduleRepeatingLockscreenWidget() {
        // Schedule repeating alarm
        AppAlarmManager.scheduleLockscreenWidgets(this)
        // Enable boot receiver to reschedule alarm on boot
        packageManager.setComponentEnabledSetting(
            ComponentName(this, BootReceiver::class.java),
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP
        )
    }

    private fun cancelRepeatingLockscreenWidget() {
        // Cancel repeating alarm
        AppAlarmManager.cancelLockscreenWidgets(this)
        // Disable boot receiver
        packageManager.setComponentEnabledSetting(
            ComponentName(this, BootReceiver::class.java),
            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
            PackageManager.DONT_KILL_APP
        )
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun MainActivityContent(
    onShowDelayed: () -> Unit,
    onSchedule: () -> Unit,
    onCancelSchedule: () -> Unit,
) {
    // Permission state for Android >= 13
    val permissionState = rememberMultiplePermissionsState(permissions = buildList {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            add(Manifest.permission.POST_NOTIFICATIONS)
        }
    })

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 32.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp, alignment = Alignment.CenterVertically),
    ) {
        Text(text = "Request for notification permission\n(only for Android >= 13)")
        Button(onClick = { permissionState.launchMultiplePermissionRequest() }, enabled = !permissionState.allPermissionsGranted) {
            Text(text = if (permissionState.allPermissionsGranted) "Permission granted" else "Request permission")
        }
        Spacer(modifier = Modifier
            .fillMaxWidth(0.8f)
            .height(1.dp)
            .background(Color.LightGray))
        Text(text = "Schedule notification.\nThis will set a delay of 5 seconds before notification shows up.\n" +
                "Quickly turn off screen after pressing this button and wait for notification.")
        Button(onClick = onShowDelayed, enabled = permissionState.allPermissionsGranted) {
            Text(text = "Schedule delayed")
        }
        Spacer(modifier = Modifier
            .fillMaxWidth(0.8f)
            .height(1.dp)
            .background(Color.LightGray))
        Text(text = "Schedule repeating alarm.\n" +
                "This will set an alarm at 6 A.M. everyday (with an amount of delay depends on system).\n" +
                "Simply set system clock to this time to test.")
        Button(onClick = onSchedule, enabled = permissionState.allPermissionsGranted) {
            Text(text = "Schedule repeating")
        }
        Spacer(modifier = Modifier
            .fillMaxWidth(0.8f)
            .height(1.dp)
            .background(Color.LightGray))
        Text(text = "Cancel repeating alarm.")
        Button(onClick = onCancelSchedule, enabled = permissionState.allPermissionsGranted) {
            Text(text = "Cancel repeating")
        }
    }
}