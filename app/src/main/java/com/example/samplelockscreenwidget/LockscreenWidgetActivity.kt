package com.example.samplelockscreenwidget

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.samplelockscreenwidget.component.DigitalClock

class LockscreenWidgetActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showOnLockscreen()

        setContent {
            LockscreenWidgetScreenContent(
                onWidgetClick = {
                    Toast.makeText(this, "You clicked on the widget", Toast.LENGTH_SHORT).show()
                },
                onDismissClick = {
                    finish()
                }
            )
        }
    }

    private fun showOnLockscreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
        } else {
            window.addFlags(WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON)
        }
    }
}

@Composable
private fun LockscreenWidgetScreenContent(
    onWidgetClick: () -> Unit,
    onDismissClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp)
            .padding(bottom = 10.dp)
            .navigationBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Spacer(modifier = Modifier.weight(1f))
        DigitalClock()
        Spacer(modifier = Modifier.weight(2f))
        IconButton(
            onClick = onDismissClick,
            modifier = Modifier
                .clip(CircleShape)
                .size(24.dp)
                .padding(2.dp)
                .background(Color(0xCC8D8D8D))
                .align(Alignment.End),
        ) {
            Icon(
                Icons.Default.Close,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = Color.White,
            )
        }
        SampleWidget(onWidgetClick = onWidgetClick)
    }
}

@Composable
private fun SampleWidget(
    onWidgetClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .clickable { onWidgetClick() }
            .background(Color.White)
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.headlineSmall,
        )
        Text(
            text = stringResource(R.string.widget_title),
            style = MaterialTheme.typography.headlineSmall,
        )
        Image(
            painter = painterResource(R.drawable.sample_image),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.35f)
                .clip(RoundedCornerShape(20.dp)),
            contentScale = ContentScale.FillBounds,
        )
    }
}

@Preview
@Composable
private fun PreviewLockscreenWidgetActivity() {
    LockscreenWidgetScreenContent({}, {})
}