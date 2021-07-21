package com.example.application.list.viewmodels

import android.annotation.SuppressLint
import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.application.BuildConfig
import com.example.application.api.LocationResponse
import com.example.application.api.LocationService
import com.example.application.api.WeatherResponse
import com.example.application.api.WeatherService
import com.example.application.models.Weather
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import java.net.SocketTimeoutException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.floor

class WeatherListViewModel(
    private val weatherService: WeatherService,
    private val locationService: LocationService,
    private val gpsLocationTask: Task<Location>
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

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val call: Call<WeatherResponse> = weatherService.getCurrentWeatherData(
                    q = currentCity,
                    lat = lat,
                    lon = lon,
                    app_id = BuildConfig.OWM_API_KEY
                )

                val response = call.execute()

                if (response.isSuccessful) {
                    if (response.body() == null) {
                        _title.postValue("city not found")
                        _header.postValue("")
                        _reload.postValue(false)
                        return@launch
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

                        if (lastTime != checkTime.toInt()) {
                            weather.add(
                                Weather.responseConvert(
                                    weatherItem,
                                    weatherResponse,
                                    time,
                                    checkTime
                                )
                            )
                        }

                        lastTime = checkTime.toInt()
                    }

                    _data.postValue(weather)
                }

                _reload.postValue(false)

            } catch (e: Exception) {
                _title.postValue("no internet connection  pull to refresh")
                _reload.postValue(false)
            } catch (e: SocketTimeoutException) {
                _title.postValue("bad internet connection")
                _reload.postValue(false)
            }
        }
    }

    private fun loadLocation() {
        viewModelScope.launch(Dispatchers.IO) {
            gpsLocationTask.addOnSuccessListener {
                if (it != null) {
                    lat = it.latitude.toString()
                    lon = it.longitude.toString()
                    loadData()
                    return@addOnSuccessListener
                }
            }

            try {
                val call: Call<LocationResponse> = locationService.getLocation()
                val response: Response<LocationResponse> = call.execute()
                if (response.isSuccessful) {
                    val locationResponse = response.body()!!
                    lat = locationResponse.lat.toString()
                    lon = locationResponse.lon.toString()
                    loadData()
                }
            } catch (e: Exception) {
                _title.postValue("no internet connection  pull to refresh")
                _reload.postValue(false)
            } catch (e: SocketTimeoutException) {
                _title.postValue("bad internet connection")
                _reload.postValue(false)
            }
        }
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
        const val KELVIN = 272.15
    }
}