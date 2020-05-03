package com.brskzr.covidapp.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brskzr.covidapp.data.ApiResult
import com.brskzr.covidapp.data.CovidApi
import com.brskzr.covidapp.data.RetrofitClient
import com.brskzr.covidapp.data.Status
import com.brskzr.covidapp.extensions.getLanguageCode
import com.brskzr.covidapp.model.CountryModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response

class MainActivityViewModel : ViewModel(){
    private var countriesResult : MutableLiveData<ApiResult<List<CountryModel>?>> =
        MutableLiveData(ApiResult<List<CountryModel>?>(Status.WAITING, null))

    fun getCountries() : LiveData<ApiResult<List<CountryModel>?>> {
        viewModelScope.launch {
            RetrofitClient.getInstance().create(CovidApi::class.java)
                .getCountries().enqueue(object : retrofit2.Callback<List<CountryModel>> {
                    override fun onFailure(call: Call<List<CountryModel>>, t: Throwable) {
                        countriesResult.postValue(ApiResult<List<CountryModel>?>(Status.ERROR, null))
                    }
                    override fun onResponse(call: Call<List<CountryModel>>, response: Response<List<CountryModel>>) {
                        val iso = getLanguageCode()
                        var currentCountry : CountryModel? = null
                        val result = response.body()?.let {
                            currentCountry = it.firstOrNull { cm -> cm.ISO2.toLowerCase() ==  iso }
                            it.takeWhile { it.ISO2.toLowerCase() != iso }.sortedBy { it.Country }.toMutableList()
                        } ?: mutableListOf()

                        currentCountry?.let {
                            result.add(0, it)
                        }

                        countriesResult.postValue(ApiResult<List<CountryModel>?>(Status.SUCCESS, result))
                    }
                })
        }

        return countriesResult
    }

}