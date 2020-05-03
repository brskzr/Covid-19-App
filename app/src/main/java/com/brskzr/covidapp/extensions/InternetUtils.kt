package com.brskzr.covidapp.extensions

import android.content.Context
import android.net.ConnectivityManager
import com.brskzr.covidapp.data.RetrofitClient
import kotlinx.coroutines.*
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL


object InternetUtils {

    private fun hasNetworkAvailable(context: Context): Boolean {
        val service = Context.CONNECTIVITY_SERVICE
        val manager = context.getSystemService(service) as ConnectivityManager?
        val network = manager?.activeNetworkInfo
        return (network?.isConnected) ?: false
    }

    fun hasInternetConnected(context: Context): Boolean {
        if (hasNetworkAvailable(context)) {
            try {
                val connection = URL("https://www.google.com").openConnection() as HttpURLConnection
                connection.setRequestProperty("User-Agent", "Test")
                connection.setRequestProperty("Connection", "close")
                connection.connectTimeout = 1500
                connection.connect()
                return (connection.responseCode == 200)
            } catch (e: IOException) {
            }
        }

        return false
    }

    fun checkServerConnectedAsync(
        context: Context,
        onConnect: () -> Unit,
        connectionNotFound: () -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            if (hasNetworkAvailable(context)) {

                try {
                    val connection = URL(RetrofitClient.BASE_URL).openConnection() as HttpURLConnection
                    connection.setRequestProperty("User-Agent", "Test")
                    connection.setRequestProperty("Connection", "close")
                    connection.connectTimeout = 5000
                    connection.connect()
                    if (connection.responseCode == 200) {
                        withContext(Dispatchers.Main) {
                            onConnect()
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            connectionNotFound()
                        }
                    }
                } catch (e: IOException) {
                    withContext(Dispatchers.Main) {
                        connectionNotFound()
                    }
                }
            } else {
                withContext(Dispatchers.Main) {
                    connectionNotFound()
                }
            }
        }



    }


}
