package com.brskzr.covidapp

import android.app.Application
import com.brskzr.covidapp.notification.NotificationUtils

class App : Application() {


    override fun onCreate() {
        super.onCreate()
        NotificationUtils(this.applicationContext).createChannel()
    }
}