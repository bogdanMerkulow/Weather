package com.example.application.list.viewmodels

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.application.BuildConfig
import com.example.application.WeatherResponse
import com.example.application.api.LocationResponse
import com.example.application.api.LocationService
import com.example.application.api.WeatherService
import com.example.application.models.Weather
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.floor

class WeatherListViewModel(
    private val weatherService: WeatherService,
    private val locationService: LocationService
) : ViewModel() {
    private val _data: MutableLiveData<List<Weather>> = MutableLiveData<List<Weather>>()
    private val _reload: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    private val _title: MutableLiveData<String> = MutableLiveData<String>()
    private val _header: MutableLiveData<String> = MutableLiveData<String>()
    private val _headerImageUrl: MutableLiveData<String> = MutableLiveData<String>()
    private var currentCity: String = String()
    private var lat: String = String()
    private var lon: String = String()

    val data: LiveData<List<Weather>>
        get() = _data

    val reload: LiveData<Boolean>
        get() = _reload

    val title: LiveData<String>
        get() = _title

    val header: LiveData<String>
        get() = _header

    val headerImageUrl: LiveData<String>
        get() = _headerImageUrl

    init {
        loadLocation()
    }

    fun loadData() {
        _reload.postValue(true)
        val call: Call<WeatherResponse> = weatherService.getCurrentWeatherData(
            q = currentCity,
            lat = lat,
            lon = lon,
            app_id = BuildConfig.OWM_API_KEY
        )

        call.enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(
                call: Call<WeatherResponse>,
                response: Response<WeatherResponse>
            ) {
                if (response.body() == null) {
                    _title.postValue("city not found")
                    _header.postValue("")
                    _reload.postValue(false)
                    return
                }

                val weatherResponse = response.body()!!
                val weather = mutableListOf<Weather>()
                var lastTime = 0

                _title.postValue(weatherResponse.city.name)
                _header.postValue(floor(weatherResponse.list[0].main.temp - KELVIN).toString() + "°C")
                _headerImageUrl.postValue("https://openweathermap.org/img/wn/${weatherResponse.list[0].weather[0].icon}@4x.png")

                weatherResponse.list.forEach { weatherItem ->
                    val date = weatherItem.dt?.toLong()?.times(1000)?.let { Date(it) }
                    val time = dateFormatTimeStamp.format(date)
                    val checkTime = dateFormatDay.format(date)

                    if (!lastTime.equals(checkTime.toInt())) {
                        weather.add(
                            Weather.responseConvert(weatherItem, weatherResponse, time, checkTime)
                        )
                    }

                    lastTime = checkTime.toInt()
                }

                _data.postValue(weather)
                _reload.postValue(false)
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                _title.postValue("no internet connection  pull to refresh")
                _reload.postValue(false)
            }
        })
    }

    fun loadLocation() {
        val call: Call<LocationResponse> = locationService.getLocation()

        call.enqueue(object : Callback<LocationResponse> {
            override fun onResponse(
                call: Call<LocationResponse>,
                response: Response<LocationResponse>
            ) {
                if (response.code() == RESPONSE_CODE_OK) {
                    val locationResponse = response.body()!!
                    lat = locationResponse.lat.toString()
                    lon = locationResponse.lon.toString()
                    loadData()
                }
            }

            override fun onFailure(call: Call<LocationResponse>, t: Throwable) {
                _title.postValue("no internet connection  pull to refresh")
                _reload.postValue(false)
            }
        })
    }

    fun changeLocation(city: String) {
        currentCity = city
        loadData()
    }

    companion object {
        @SuppressLint("SimpleDateFormat")
        val dateFormatTimeStamp = SimpleDateFormat("E dd.MM hh:mm")

        @SuppressLint("SimpleDateFormat")
        val dateFormatDay = SimpleDateFormat("dd")
        const val RESPONSE_CODE_OK = 200
        const val KELVIN = 272.15
    }
}