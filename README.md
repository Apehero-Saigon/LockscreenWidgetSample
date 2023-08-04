# Lockscreen Widget

The widget we are showing on lockscreen isn't an actual widget, it's our Activity that look like a lockscreen and we put a View on it.

In order start an activity when there is no foreground component, we have to attach it to a notification by calling "setFullscreenIntent" when
building the notification and allow the activity to show on lockscreen in the activity.

## Step by step:

- First we need some permissions:

```xml

<manifest>
    <uses-permission android:name="android.permission.POST_NOTIFICATIONS" />
    <uses-permission android:name="android.permission.USE_FULL_SCREEN_INTENT" />
</manifest>
```

- Now we create an activity that looks like lockscreen with our desired view on it. Take a look at my sample "LockscreenWidgetActivity".
- Put the follow block of code into the activity and call it in onCreate to allow the activity to show on lockscreen.

```kotlin
private fun showOnLockscreen() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
        setShowWhenLocked(true)
    } else {
        window.addFlags(WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON)
    }
}
```

- To make the activity looks like lockscreen, we have to put some attributes into activity's theme:
```xml

<style name="FullscreenReminderTheme" parent="Theme.AppCompat.NoActionBar">
    <item name="android:windowBackground">@android:color/transparent</item>
    <item name="android:colorBackgroundCacheHint">@null</item>
    <item name="android:windowShowWallpaper">true</item>
    <item name="android:windowTranslucentNavigation">true</item>
    <item name="android:windowTranslucentStatus">true</item>
</style>
```

- And we also want to exclude the Activity from recent applications. Put these into Activity's manifest:
```xml
<activity
    android:name=".LockscreenWidgetActivity"
    android:exported="false"
    android:excludeFromRecents="true"
    android:launchMode="singleInstance"
    android:noHistory="true"
    android:theme="@style/FullscreenReminderTheme"
/>
```

- Now let's build the notification and put the intent to launch activity into it
```kotlin
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
```

- When the notification is fired, there are two situations:
    - If device is in lockscreen (regardless the screen is on or not): the activity will show over the lockscreen.
    - If device is not in lockscreen: a notification is shown instead.

## Note:
- In demo code, when the button is clicked, I set a delay of 5 seconds before the notification is fired.
- In real situation, you should schedule to fire the notification using schedule mechanics. Currently I'm using AlarmManager.

## TODO: Tutorial on how to use AlarmManager to schedule notification
