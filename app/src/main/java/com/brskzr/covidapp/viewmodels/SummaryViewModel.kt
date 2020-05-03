package com.brskzr.covidapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brskzr.covidapp.data.ApiResult
import com.brskzr.covidapp.data.CovidApi
import com.brskzr.covidapp.data.RetrofitClient
import com.brskzr.covidapp.data.Status
import com.brskzr.covidapp.extensions.getLanguageCode
import com.brskzr.covidapp.model.*
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response

class SummaryViewModel : ViewModel() {
    private var worldTotalData : MutableLiveData<ApiResult<WorldTotalSummaryModel?>> =
        MutableLiveData(ApiResult<WorldTotalSummaryModel?>(Status.WAITING, null))

    private var summaryData: MutableLiveData<ApiResult<SummaryModel?>> =
        MutableLiveData(ApiResult<SummaryModel?>(Status.WAITING, null))

    private var summaryOfCountry: MutableLiveData<ApiResult<List<SummaryOfCountryModel>?>> =
        MutableLiveData(ApiResult<List<SummaryOfCountryModel>?>(Status.WAITING, null))

    private val api : CovidApi = RetrofitClient.getInstance().create(CovidApi::class.java)

    fun getToalWorldSummary() : LiveData<ApiResult<WorldTotalSummaryModel?>> {
        viewModelScope.launch {
             api.getWorlTotalSummary().enqueue(object : retrofit2.Callback<WorldTotalSummaryModel> {
                    override fun onFailure(call: Call<WorldTotalSummaryModel>, t: Throwable) {
                        worldTotalData.postValue(ApiResult<WorldTotalSummaryModel?>(Status.ERROR, null))
                    }
                    override fun onResponse(call: Call<WorldTotalSummaryModel>, response: Response<WorldTotalSummaryModel>) {
                        worldTotalData.postValue(ApiResult<WorldTotalSummaryModel?>(Status.SUCCESS, response.body()))
                    }
                })
        }

        return worldTotalData
    }

    fun getSummary(): LiveData<ApiResult<SummaryModel?>> {
        viewModelScope.launch {
                api.getSummary().enqueue(object : retrofit2.Callback<SummaryModel> {
                    override fun onFailure(call: Call<SummaryModel>, t: Throwable) {
                        summaryData.postValue(ApiResult<SummaryModel?>(Status.ERROR, null))
                    }
                    override fun onResponse(call: Call<SummaryModel>, response: Response<SummaryModel>) {
                        summaryData.postValue(ApiResult<SummaryModel?>(Status.SUCCESS, response.body()))
                    }
                })
        }

        return summaryData
    }

    fun getSummaryOfCountry(slug: String) : LiveData<ApiResult<List<SummaryOfCountryModel>?>> {
        viewModelScope.launch {
            api.getSummaryOfCountry(slug).enqueue(object : retrofit2.Callback<List<SummaryOfCountryModel>> {
                override fun onFailure(call: Call<List<SummaryOfCountryModel>>, t: Throwable) {
                    summaryOfCountry.postValue(ApiResult<List<SummaryOfCountryModel>?>(Status.ERROR, null))
                }
                override fun onResponse(call: Call<List<SummaryOfCountryModel>>, response: Response<List<SummaryOfCountryModel>>) {
                    summaryOfCountry.postValue(ApiResult<List<SummaryOfCountryModel>?>(Status.SUCCESS, response.body()))
                }
            })
        }

        return summaryOfCountry
    }

    override fun onCleared() {
        super.onCleared()
        worldTotalData.value = null
        summaryData.value = null
    }
}