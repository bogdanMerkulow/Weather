package com.example.application.detail.viewmodels

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.application.api.WeatherResponse
import com.example.application.api.WeatherService
import com.example.application.extensions.toWeather
import com.example.application.models.Weather
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.net.SocketTimeoutException
import java.text.SimpleDateFormat

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
            val weatherResponse: WeatherResponse = weatherService.getCurrentWeatherData(city = q)

            if (weatherResponse.message != CITY_NOT_FOUND) {
                val weather = mutableListOf<Weather>()

                Timber.i("response successful weather items count for ${weatherResponse.city.name}: ${weatherResponse.list.size}")

                weatherResponse.list.forEach { weatherItem ->
                    val unixTimestamp = weatherItem.dt.times(1000)
                    val currentDay = dateFormatDay.format(unixTimestamp)

                    if (selectedDay == currentDay) {
                        weather.add(weatherItem.toWeather(TIME_FORMAT))
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
        private val dateFormatDay = SimpleDateFormat("dd")

        private const val TIME_FORMAT = "hh:mm"
        private const val DEFAULT_DAY = "0"
        private const val DEFAULT_CITY = ""
        private const val NO_INTERNET = "no internet connection  pull to refresh"
        private const val BAD_INTERNET = "bad internet connection"
        private const val CITY_NOT_FOUND = "city not found"
        private const val VISIBLE = 0
        private const val INVISIBLE = 4
    }
}