package com.example.application.detail.viewmodels

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.application.*
import com.example.application.api.WeatherService
import com.example.application.models.Weather
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.floor

class WeatherDetailViewModel(private val weatherService: WeatherService) : ViewModel() {
    private val _data: MutableLiveData<List<Weather>> = MutableLiveData<List<Weather>>()
    private val _reload: MutableLiveData<Boolean> = MutableLiveData<Boolean>()

    val data: LiveData<List<Weather>>
        get() = _data

    val reload: LiveData<Boolean>
        get() = _reload

    fun loadData(q: String, day: String = "0") {
        _reload.postValue(true)

        val call: Call<WeatherResponse> = weatherService.getCurrentWeatherData(q = q, app_id = APP_ID)

        call.enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                val weatherResponse = response.body()!!
                val weather =  mutableListOf<Weather>()

                weatherResponse.list.forEach{ weatherItem ->
                    val date = weatherItem.dt?.toLong()?.times(1000)?.let { Date(it) }
                    val time = dateFormatTimeStamp.format(date)
                    val checkTime = dateFormatDay.format(date)

                    if(day.equals(checkTime)) {
                        weather.add(
                            weatherResponseToWeather(weatherItem, weatherResponse, time, checkTime)
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

    private fun weatherResponseToWeather(
        weatherItem: WeatherList,
        weatherResponse: WeatherResponse,
        time: String,
        checkTime: String
    ): Weather {
        return Weather(
            iconName = weatherItem.weather[0].icon,
            title = time,
            temp = (floor(weatherItem.main.temp - KELVIN)).toFloat(),
            state = weatherItem.weather[0].description,
            city = weatherResponse.city.name,
            lat = weatherResponse.city.coord?.lat.toString(),
            lon = weatherResponse.city.coord?.lon.toString(),
            dayNumber = checkTime
        )
    }

    companion object {
        @SuppressLint("SimpleDateFormat")
        val dateFormatTimeStamp = SimpleDateFormat("E dd.MM hh:mm")
        @SuppressLint("SimpleDateFormat")
        val dateFormatDay = SimpleDateFormat("dd")
        const val BASE_URL = "https://api.openweathermap.org/"
        const val APP_ID = "c46b6b253436ddd455030408be9b19bf"
        const val KELVIN = 272.15
    }
}