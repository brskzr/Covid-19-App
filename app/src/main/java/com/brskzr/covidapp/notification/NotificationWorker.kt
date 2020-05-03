package com.brskzr.covidapp.notification

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.brskzr.covidapp.data.ApiResult
import com.brskzr.covidapp.data.CovidApi
import com.brskzr.covidapp.data.RetrofitClient
import com.brskzr.covidapp.data.Status
import com.brskzr.covidapp.extensions.formatDotted
import com.brskzr.covidapp.model.SummaryModel
import retrofit2.Call
import retrofit2.Response
import java.lang.Exception


class NotificationWorker(appContext: Context, workerParams: WorkerParameters) : Worker(appContext, workerParams) {

    override fun doWork(): Result {

        try {
            getLatestResults()
            return Result.success()
        }
        catch (ex :Exception) {
            return  Result.failure()
        }
    }

    private fun sendNotification(content: String) {
        NotificationUtils(applicationContext).notify(content)
    }

    private fun getLatestResults() {

        RetrofitClient.getInstance().create(CovidApi::class.java).getSummary().enqueue(object : retrofit2.Callback<SummaryModel> {
            override fun onFailure(call: Call<SummaryModel>, t: Throwable) {}

            override fun onResponse(call: Call<SummaryModel>, response: Response<SummaryModel>) {
                val data = response.body()
                data?.let {
                    try {
                        val content = "New results of World: " +
                                "Confirmed ${it.Global.NewConfirmed.formatDotted()} people , " +
                                "Deaths ${it.Global.NewDeaths.formatDotted()} people, " +
                                "Recovered ${it.Global.NewRecovered.formatDotted()} people"

                        sendNotification(content)
                    }
                    catch (ex :Exception) {

                    }
                }
            }
        })
    }

}

