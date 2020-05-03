package com.brskzr.covidapp.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient {

    companion object {
        const val COUNTRIES = "countries"
        const val BASE_URL = "https://api.covid19api.com/"
        private var instance : Retrofit? = null

        @Synchronized
        fun getInstance() : Retrofit {
            if(instance == null) {
                instance = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }

            return instance!!
        }
    }
}