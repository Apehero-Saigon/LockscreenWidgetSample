package com.example.samplelockscreenwidget.component

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

val DateFormatter: DateFormat = SimpleDateFormat.getDateInstance(DateFormat.MEDIUM)
val TimeFormatter: DateFormat = SimpleDateFormat.getTimeInstance(DateFormat.SHORT)

@Composable
fun DigitalClock(modifier: Modifier = Modifier) {
    var time by remember { mutableStateOf(Date()) }
    val clock by remember { derivedStateOf { TimeFormatter.format(time) } }
    val date by remember { derivedStateOf { DateFormatter.format(time) } }

    LaunchedEffect(Unit) {
        while (true) {
            if (!isActive) break
            delay(1000)
            time = Date()
        }
    }

    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = clock,
            style = TextStyle(
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight(300),
                fontSize = 70.sp,
                lineHeight = 75.sp,
                color = Color.White,
            ),
        )
        Text(
            text = date,
            style = TextStyle(
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight(300),
                fontSize = 16.sp,
                lineHeight = 24.sp,
                color = Color.White,
            ),
        )
    }
}

@Preview
@Composable
private fun PreviewDigitalClock() {
    DigitalClock()
}