package com.example.application.detail.viewmodels

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.application.BuildConfig
import com.example.application.api.WeatherResponse
import com.example.application.api.WeatherService
import com.example.application.models.Weather
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class WeatherDetailViewModel(private val weatherService: WeatherService) : ViewModel() {
    private val _data: MutableLiveData<List<Weather>> = MutableLiveData<List<Weather>>()
    private val _reload: MutableLiveData<Boolean> = MutableLiveData<Boolean>()

    val data: LiveData<List<Weather>>
        get() = _data

    val reload: LiveData<Boolean>
        get() = _reload

    fun loadData(q: String, day: String = "0") {
        _reload.postValue(true)

        val call: Call<WeatherResponse> =
            weatherService.getCurrentWeatherData(q = q, app_id = BuildConfig.OWM_API_KEY)

        call.enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(
                call: Call<WeatherResponse>,
                response: Response<WeatherResponse>
            ) {
                val weatherResponse = response.body()!!
                val weather = mutableListOf<Weather>()

                weatherResponse.list.forEach { weatherItem ->
                    val date = weatherItem.dt?.toLong()?.times(1000)?.let { Date(it) }
                    val time = dateFormatTimeStamp.format(date)
                    val checkTime = dateFormatDay.format(date)

                    if (day == checkTime) {
                        weather.add(
                            Weather.responseConvert(weatherItem, weatherResponse, time, checkTime)
                        )
                    }
                }

                _data.postValue(weather)
                _reload.postValue(false)
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                _reload.postValue(false)
            }
        })
    }

    companion object {
        @SuppressLint("SimpleDateFormat")
        val dateFormatTimeStamp = SimpleDateFormat("E dd.MM hh:mm")

        @SuppressLint("SimpleDateFormat")
        val dateFormatDay = SimpleDateFormat("dd")
    }
}