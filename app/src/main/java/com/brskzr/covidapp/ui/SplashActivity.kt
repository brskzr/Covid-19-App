package com.brskzr.covidapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.brskzr.covidapp.R
import com.brskzr.covidapp.extensions.InternetUtils
import com.brskzr.covidapp.extensions.toaster
import kotlinx.coroutines.*

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)


        InternetUtils.checkServerConnectedAsync(this,
            onConnect =  {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            },
            connectionNotFound =  {
                toaster("Error")
                CoroutineScope(Dispatchers.Main).launch {
                    delay(3000)
                    finish()
                }
            })
    }

}
