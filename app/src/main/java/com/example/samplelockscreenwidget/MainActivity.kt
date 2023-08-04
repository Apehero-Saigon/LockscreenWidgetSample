package com.example.samplelockscreenwidget

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppNotificationManager.createNotificationChannels(this)

        setContent {
            MainActivityContent {
                Toast.makeText(this, "Scheduled in 5 seconds", Toast.LENGTH_SHORT).show()
                scheduleLockscreenWidgetNotification()
            }
        }
    }

    private fun scheduleLockscreenWidgetNotification() {
        lifecycleScope.launch {
            delay(5000)
            AppNotificationManager.sendLockscreenWidgetNotification(this@MainActivity)
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun MainActivityContent(
    onButtonClick: () -> Unit,
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
        verticalArrangement = Arrangement.spacedBy(12.dp, alignment = Alignment.CenterVertically)
    ) {
        Text(text = "1. Request for notification permission\n(only for Android >= 13)")
        Button(onClick = { permissionState.launchMultiplePermissionRequest() }, enabled = !permissionState.allPermissionsGranted) {
            Text(text = if (permissionState.allPermissionsGranted) "Permission granted" else "Request permission")
        }
        Text(text = "2. Schedule notification.\nThis will set a delay of 5 seconds before notification shows up")
        Button(onClick = onButtonClick, enabled = permissionState.allPermissionsGranted) {
            Text(text = stringResource(R.string.schedule_notification))
        }
    }
}