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
import timber.log.Timber
import java.net.SocketTimeoutException
import java.text.SimpleDateFormat
import java.util.*

class WeatherDetailViewModel(private val weatherService: WeatherService) : ViewModel() {
    private val _data: MutableLiveData<List<Weather>> = MutableLiveData<List<Weather>>()
    private val _reload: MutableLiveData<Int> = MutableLiveData<Int>()
    private val _title: MutableLiveData<String> = MutableLiveData<String>()

    val title: LiveData<String>
        get() = _title

    val data: LiveData<List<Weather>>
        get() = _data

    val reload: LiveData<Int>
        get() = _reload

    fun loadData(
        q: String = DEFAULT_CITY,
        selectedDay: String = DEFAULT_DAY
    ) = viewModelScope.launch(Dispatchers.IO) {
        _reload.postValue(VISIBLE)

        try {
            val call: Call<WeatherResponse> = weatherService.getCurrentWeatherData(
                city = q,
                api_key = BuildConfig.OWM_API_KEY
            )
            val response = call.execute()

            if (response.isSuccessful) {
                val weatherResponse = response.body()!!
                val weather = mutableListOf<Weather>()

                Timber.i("response successful weather items count for ${weatherResponse.city.name}: ${weatherResponse.list.size}")

                weatherResponse.list.forEach { weatherItem ->
                    val date = weatherItem.dt?.toLong()?.times(1000)?.let { Date(it) }
                    val currentDay = dateFormatDay.format(date)

                    if (selectedDay == currentDay) {
                        weather.add(Weather.responseConvert(weatherItem))
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

        _reload.postValue(INVISIBLE)
    }

    companion object {
        @SuppressLint("SimpleDateFormat")
        val dateFormatDay = SimpleDateFormat("dd")

        const val DEFAULT_DAY = "0"
        const val DEFAULT_CITY = ""
        const val NO_INTERNET = "no internet connection  pull to refresh"
        const val BAD_INTERNET = "bad internet connection"
        const val VISIBLE = 0
        const val INVISIBLE = 4
    }
}