package com.brskzr.covidapp.notification

import android.content.Context
import androidx.work.*
import java.util.*
import java.util.concurrent.TimeUnit

object NotificationWorkFactory {

    const val TAG = "notification.work"

    fun create(context: Context, period: NotificationPeriod) {
        val constraints = Constraints.Builder()
            .setRequiresCharging(false)
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(true)
            .build()

        var repeatInterval = 1L
        var timeUnit = TimeUnit.DAYS

        when(period){
            NotificationPeriod.Hourly -> {
                repeatInterval = 1
                timeUnit = TimeUnit.HOURS
            }
            NotificationPeriod.PerFourHour -> {
                repeatInterval = 4
                timeUnit = TimeUnit.HOURS
            }
            NotificationPeriod.EndOfDay -> {
                repeatInterval = 1
                timeUnit = TimeUnit.DAYS
            }
        }

        val workBuilder = PeriodicWorkRequestBuilder<NotificationWorker>(repeatInterval, timeUnit)
            .setConstraints(constraints)
            .addTag(TAG)

        if(period == NotificationPeriod.EndOfDay) {
            val endOfDay = Calendar.getInstance().apply {
                add(Calendar.HOUR, 22)
                add(Calendar.MINUTE, 30)
            }

            val delayInMillis = endOfDay.timeInMillis - Calendar.getInstance().timeInMillis
            if(delayInMillis > 0) {
                workBuilder.setInitialDelay(delayInMillis, TimeUnit.MILLISECONDS)
            }
        }
        else{
            workBuilder.setInitialDelay(repeatInterval, timeUnit)
        }

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(TAG, ExistingPeriodicWorkPolicy.REPLACE, workBuilder.build())
    }

    fun cancel(context: Context) {
        WorkManager.getInstance(context).cancelAllWorkByTag(TAG)
    }
}

enum class NotificationPeriod { None, Hourly, PerFourHour, EndOfDay }
