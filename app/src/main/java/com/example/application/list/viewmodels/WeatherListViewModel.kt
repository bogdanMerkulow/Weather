package com.example.application.list.viewmodels

import android.annotation.SuppressLint
import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.application.api.LocationResponse
import com.example.application.api.LocationService
import com.example.application.api.WeatherResponse
import com.example.application.api.WeatherService
import com.example.application.models.Coords
import com.example.application.models.Weather
import com.example.application.models.Weather.Companion.toWeather
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import timber.log.Timber
import java.net.SocketTimeoutException
import java.text.SimpleDateFormat
import java.util.*

class WeatherListViewModel(
    private val weatherService: WeatherService,
    private val locationService: LocationService,
    private val gpsLocationTask: Task<Location>
) : ViewModel() {
    private val _data: MutableLiveData<List<Weather>> = MutableLiveData<List<Weather>>()
    private val _reload: MutableLiveData<Int> = MutableLiveData<Int>()
    private val _title: MutableLiveData<String> = MutableLiveData<String>()
    private val _header: MutableLiveData<String> = MutableLiveData<String>()
    private val _headerImageUrl: MutableLiveData<String> = MutableLiveData<String>()
    private val _clickData: MutableLiveData<Map<String, String>> =
        MutableLiveData<Map<String, String>>()
    private var currentCity: String = String()
    private var coords: Coords = Coords()

    val data: LiveData<List<Weather>>
        get() = _data

    val reload: LiveData<Int>
        get() = _reload

    val title: LiveData<String>
        get() = _title

    val header: LiveData<String>
        get() = _header

    val headerImageUrl: LiveData<String>
        get() = _headerImageUrl

    val clickData: LiveData<Map<String, String>>
        get() = _clickData

    fun loadData() = viewModelScope.launch(Dispatchers.IO) {
        _reload.postValue(VISIBLE)

        try {
            val call: Call<WeatherResponse> = weatherService.getCurrentWeatherData(
                city = currentCity,
                latitude = coords.latitude,
                longitude = coords.longitude
            )

            val response = call.execute()

            if (response.isSuccessful) {
                val weatherResponse = response.body()!!
                val weather = mutableListOf<Weather>()
                var lastDay = 0

                currentCity = weatherResponse.city.name
                _title.postValue(weatherResponse.city.name)
                _header.postValue(Weather(temp = weatherResponse.list[0].main.temp - KELVIN.toInt()).getTemp())
                _headerImageUrl.postValue(Weather(iconName = weatherResponse.list[0].weather[0].icon).getIconUrl())

                Timber.i("response successful weather items count for ${weatherResponse.city.name}: ${weatherResponse.list.size}")

                weatherResponse.list.forEach { weatherItem ->
                    val unixTimestamp = weatherItem.dt?.toLong()?.times(1000)?.let { Date(it) }
                    val currentDay = dateFormatDay.format(unixTimestamp)

                    if (lastDay != currentDay.toInt()) {
                        weather.add(weatherItem.toWeather())
                    }

                    lastDay = currentDay.toInt()
                }

                _data.postValue(weather)
            } else {
                _title.postValue(NO_CITY)
                _header.postValue(DEFAULT_CITY)
                _reload.postValue(INVISIBLE)
                Timber.i("City $currentCity not found")
                return@launch
            }

        } catch (e: Exception) {
            Timber.i("No internet connection")
            _title.postValue(NO_INTERNET)
        } catch (e: SocketTimeoutException) {
            Timber.i("Connection timeout error")
            _title.postValue(BAD_INTERNET)
        }

        _reload.postValue(INVISIBLE)
    }

    fun loadLocation() {
        gpsLocationTask.addOnSuccessListener {
            viewModelScope.launch(Dispatchers.IO) {
                if (it != null) {
                    coords.latitude = it.latitude.toString()
                    coords.longitude = it.longitude.toString()
                    Timber.i("GPS location: lat: ${coords.latitude} / lon: ${coords.longitude}")
                    loadData()
                } else {
                    try {
                        val call: Call<LocationResponse> = locationService.getLocation()
                        val response: Response<LocationResponse> = call.execute()
                        if (response.isSuccessful) {
                            val locationResponse = response.body()!!
                            coords.latitude = locationResponse.lat.toString()
                            coords.longitude = locationResponse.lon.toString()
                            Timber.i("IP location: lat: ${coords.latitude} / lon: ${coords.longitude}")
                            loadData()
                        }
                    } catch (e: Exception) {
                        Timber.i("No internet connection")
                        _title.postValue(NO_INTERNET)
                    } catch (e: SocketTimeoutException) {
                        Timber.i("Connection timeout error")
                        _title.postValue(BAD_INTERNET)
                    }
                }
                _reload.postValue(INVISIBLE)
            }
        }
    }

    fun changeLocation(city: String) {
        Timber.i("Location changed, new city name: $city")
        currentCity = city
        loadData()
    }

    fun itemClick(weather: Weather) {
        _clickData.postValue(
            mapOf(
                CITY to currentCity,
                DAY to weather.dayNumber
            )
        )
    }

    companion object {
        @SuppressLint("SimpleDateFormat")
        val dateFormatDay = SimpleDateFormat("dd")

        const val KELVIN = 272.15
        const val NO_CITY = "city not found"
        const val DEFAULT_CITY = ""
        const val NO_INTERNET = "no internet connection  pull to refresh"
        const val BAD_INTERNET = "bad internet connection"
        const val VISIBLE = 0
        const val INVISIBLE = 4
        const val CITY = "city"
        const val DAY = "day"
    }
}