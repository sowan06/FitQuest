package com.fitquest.app.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.fitquest.app.MainActivity
import com.fitquest.app.R
import com.google.firebase.messaging.RemoteMessage

object NotificationHelper {
    private const val CHANNEL_ID = "FITQUEST_CHANNEL"
    private const val CHANNEL_NAME = "FitQuest"
    private const val ACCENT_COLOR = 0xFFF5C518.toInt()

    fun ensureChannel(context: Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (nm.getNotificationChannel(CHANNEL_ID) != null) return
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH,
        )
        nm.createNotificationChannel(channel)
    }

    fun show(context: Context, message: RemoteMessage) {
        ensureChannel(context)
        val notification = message.notification
        val data = message.data
        val title = notification?.title ?: data["title"] ?: "FitQuest"
        val body = notification?.body ?: data["body"] ?: ""
        val type = data["type"]

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            putExtra("nav_target", type)
        }
        val pi = PendingIntent.getActivity(
            context,
            type.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(body)
            .setStyle(NotificationCompat.BigTextStyle().bigText(body))
            .setColor(ACCENT_COLOR)
            .setAutoCancel(true)
            .setContentIntent(pi)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        nm.notify((title + body).hashCode(), builder.build())
    }
}
