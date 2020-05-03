package com.brskzr.covidapp.data

import com.brskzr.covidapp.model.CountryModel
import com.brskzr.covidapp.model.SummaryModel
import com.brskzr.covidapp.model.SummaryOfCountryModel
import com.brskzr.covidapp.model.WorldTotalSummaryModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface CovidApi {

    @GET("countries")
    fun getCountries() : Call<List<CountryModel>>

    @GET("world/total")
    fun getWorlTotalSummary(): Call<WorldTotalSummaryModel>

    @GET("summary")
    fun getSummary(): Call<SummaryModel>

    @GET("total/country/{slug}")
    fun getSummaryOfCountry(@Path("slug") slug: String): Call<List<SummaryOfCountryModel>>
}