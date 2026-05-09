package com.xbot.anilibriarefresh.notifications

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.xbot.anilibriarefresh.MainActivity
import com.xbot.anilibriarefresh.R
import java.util.concurrent.atomic.AtomicInteger

object NotificationHelper {

    private const val DEEP_LINK_RELEASE_PATTERN = "anilibria://release/%s"
    private val notificationCounter = AtomicInteger(0)

    fun createNotificationChannels(context: Context) {
        val channel = NotificationChannelCompat.Builder(
            context.getString(R.string.notification_channel_release_id),
            NotificationManagerCompat.IMPORTANCE_DEFAULT,
        )
            .setName(context.getString(R.string.notification_channel_release_name))
            .setDescription(context.getString(R.string.notification_channel_release_description))
            .build()

        NotificationManagerCompat.from(context).createNotificationChannel(channel)
    }

    fun showNotification(
        context: Context,
        title: String?,
        message: String?,
        releaseId: String? = null,
    ) {
        if (title.isNullOrBlank() && message.isNullOrBlank()) return

        val notificationManager = NotificationManagerCompat.from(context)
        if (!notificationManager.areNotificationsEnabled()) return
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS,
            ) != PackageManager.PERMISSION_GRANTED
        ) return

        val channelId = context.getString(R.string.notification_channel_release_id)
        val notificationId = notificationCounter.incrementAndGet()

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_launcher_monochrome)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(buildContentIntent(context, releaseId, notificationId))
            .build()

        notificationManager.notify(notificationId, notification)
    }

    private fun buildContentIntent(
        context: Context,
        releaseId: String?,
        requestCode: Int,
    ): PendingIntent {
        val intent = if (releaseId != null) {
            Intent(
                Intent.ACTION_VIEW,
                DEEP_LINK_RELEASE_PATTERN.format(releaseId).toUri(),
                context,
                MainActivity::class.java,
            )
        } else {
            Intent(context, MainActivity::class.java)
        }.apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }

        return PendingIntent.getActivity(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )
    }
}
