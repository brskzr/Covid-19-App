package com.brskzr.covidapp.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.brskzr.covidapp.ui.MainActivity
import java.text.SimpleDateFormat
import java.util.*

class NotificationUtils(val context: Context) {
    val CHANNEL_ID = "corona"

    fun notify(content:String) {
        val content = getNotification(content)
        with(NotificationManagerCompat.from(context)) {
            notify(NotificationUtils.UniqueId(), content)
        }
    }

    fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Covid-19"
            val descriptionText = "Notify for Covid-19 results"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun getNotification(content: String) : Notification = NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
        .setContentTitle("Covid-19")
        .setContentText(content)
        .setStyle(NotificationCompat.BigTextStyle())
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setContentIntent(getPendingIntent())
        .build()

    private fun getPendingIntent() : PendingIntent {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        return  PendingIntent.getActivity(context, 0, intent, 0)
    }

    companion object {
        fun UniqueId(): Int {
            val now = Date()
            return Integer.parseInt(SimpleDateFormat("ddHHmmss", Locale.US).format(now))
        }
    }
}