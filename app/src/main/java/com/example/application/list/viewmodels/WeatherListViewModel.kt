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
import com.example.application.extensions.toWeather
import com.example.application.models.Coords
import com.example.application.models.SelectedWeather
import com.example.application.models.Weather
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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
    private val _clickData: MutableLiveData<SelectedWeather> = MutableLiveData<SelectedWeather>()
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

    val clickData: LiveData<SelectedWeather>
        get() = _clickData

    fun loadData() = viewModelScope.launch(Dispatchers.IO) {
        _reload.postValue(VISIBLE)

        try {
            val weatherResponse: WeatherResponse = weatherService.getCurrentWeatherData(
                city = currentCity,
                latitude = coords.latitude,
                longitude = coords.longitude
            )

            val weather = mutableListOf<Weather>()
            var lastDay = 0

            currentCity = weatherResponse.city.name
            _title.postValue(weatherResponse.city.name)
            _header.postValue(Weather(temp = weatherResponse.list[0].main.temp - KELVIN.toInt()).getTemp())
            _headerImageUrl.postValue(Weather(iconName = weatherResponse.list[0].weather[0].icon).getIconUrl())

            Timber.i("response successful weather items count for ${weatherResponse.city.name}: ${weatherResponse.list.size}")

            weatherResponse.list.forEach { weatherItem ->
                val unixTimestamp = weatherItem.dt.times(1000)
                val currentDay = dateFormatDay.format(unixTimestamp)

                if (lastDay != currentDay.toInt()) {
                    weather.add(weatherItem.toWeather())
                }

                lastDay = currentDay.toInt()
            }

            _data.postValue(weather)

        } catch (e: Exception) {
            Timber.i("No internet connection")
            if (e.message == "HTTP 404 Not Found") {
                _title.postValue(NO_CITY)
                _header.postValue(DEFAULT_CITY)
                _reload.postValue(INVISIBLE)
                Timber.i("City $currentCity not found")
                return@launch
            }
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
                        val response: LocationResponse = locationService.getLocation()
                        coords.latitude = response.lat.toString()
                        coords.longitude = response.lon.toString()
                        Timber.i("IP location: lat: ${coords.latitude} / lon: ${coords.longitude}")
                        loadData()

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
            SelectedWeather(
                city = currentCity,
                day = weather.dayNumber
            )
        )
    }

    companion object {
        @SuppressLint("SimpleDateFormat")
        private val dateFormatDay = SimpleDateFormat("dd")

        private const val KELVIN = 272.15
        private const val NO_CITY = "city not found"
        private const val DEFAULT_CITY = ""
        private const val NO_INTERNET = "no internet connection  pull to refresh"
        private const val BAD_INTERNET = "bad internet connection"
        private const val VISIBLE = 0
        private const val INVISIBLE = 4
    }
}