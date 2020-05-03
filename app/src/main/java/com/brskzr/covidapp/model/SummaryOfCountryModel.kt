package com.brskzr.covidapp.model

import java.util.*

data class SummaryOfCountryModel(
    val Active: Int,
    val City: String,
    val CityCode: String,
    val Confirmed: Int,
    val Country: String,
    val CountryCode: String,
    val Date: Date,
    val Deaths: Int,
    val Lat: String,
    val Lon: String,
    val Province: String,
    val Recovered: Int
)