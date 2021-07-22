package com.example.application.detail.viewmodels

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.application.BuildConfig
import com.example.application.api.WeatherResponse
import com.example.application.api.WeatherService
import com.example.application.models.Weather
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import java.net.SocketTimeoutException
import java.text.SimpleDateFormat
import java.util.*

class WeatherDetailViewModel(private val weatherService: WeatherService) : ViewModel() {
    private val _data: MutableLiveData<List<Weather>> = MutableLiveData<List<Weather>>()
    private val _reload: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    private val _title: MutableLiveData<String> = MutableLiveData<String>()

    val title: LiveData<String>
        get() = _title

    val data: LiveData<List<Weather>>
        get() = _data

    val reload: LiveData<Boolean>
        get() = _reload

    fun loadData(
        q: String = DEFAULT_CITY,
        day: String = DEFAULT_DAY
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            _reload.postValue(true)

            try {
                val call: Call<WeatherResponse> = weatherService.getCurrentWeatherData(
                    city = q,
                    api_key = BuildConfig.OWM_API_KEY
                )
                val response = call.execute()

                if (response.isSuccessful) {
                    val weatherResponse = response.body()!!
                    val weather = mutableListOf<Weather>()

                    weatherResponse.list.forEach { weatherItem ->
                        val date = weatherItem.dt?.toLong()?.times(1000)?.let { Date(it) }
                        val time = dateFormatTimeStamp.format(date)
                        val checkTime = dateFormatDay.format(date)

                        if (day == checkTime) {
                            weather.add(
                                Weather.responseConvert(
                                    weatherItem,
                                    weatherResponse,
                                    time,
                                    checkTime
                                )
                            )
                        }
                    }

                    _data.postValue(weather)
                    _title.postValue(weatherResponse.city.name)
                }

            } catch (e: Exception) {
                _title.postValue(NO_INTERNET)
            } catch (e: SocketTimeoutException) {
                _title.postValue(BAD_INTERNET)
            }

            _reload.postValue(false)
        }
    }

    companion object {
        @SuppressLint("SimpleDateFormat")
        val dateFormatTimeStamp = SimpleDateFormat("hh:mm")

        @SuppressLint("SimpleDateFormat")
        val dateFormatDay = SimpleDateFormat("dd")

        const val DEFAULT_DAY = "0"
        const val DEFAULT_CITY = ""
        const val NO_INTERNET = "no internet connection  pull to refresh"
        const val BAD_INTERNET = "bad internet connection"
    }
}