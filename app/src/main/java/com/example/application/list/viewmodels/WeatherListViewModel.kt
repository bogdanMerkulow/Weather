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
    private val _reload: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    private val _title: MutableLiveData<String> = MutableLiveData<String>()
    private val _header: MutableLiveData<String> = MutableLiveData<String>()
    private val _headerImageUrl: MutableLiveData<String> = MutableLiveData<String>()
    private var currentCity: String = String()
    private var latitude: String = String()
    private var longitude: String = String()

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

    fun loadData() = viewModelScope.launch(Dispatchers.IO) {
        _reload.postValue(true)

        try {
            val call: Call<WeatherResponse> = weatherService.getCurrentWeatherData(
                city = currentCity,
                latitude = latitude,
                longitude = longitude,
                api_key = BuildConfig.OWM_API_KEY
            )

            val response = call.execute()

            if (response.isSuccessful) {
                val weatherResponse = response.body()!!
                val weather = mutableListOf<Weather>()
                var lastTime = 0

                _title.postValue(weatherResponse.city.name)
                _header.postValue(Weather(temp = weatherResponse.list[0].main.temp - KELVIN.toInt()).getTemp())
                _headerImageUrl.postValue(Weather(iconName = weatherResponse.list[0].weather[0].icon).getIconUrl())

                Timber.i("response successful weather items count for ${weatherResponse.city.name}: ${weatherResponse.list.size}")

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
            } else {
                _title.postValue(NO_CITY)
                _header.postValue(DEFAULT_CITY)
                _reload.postValue(false)
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

        _reload.postValue(false)
    }

    private fun loadLocation() = viewModelScope.launch(Dispatchers.IO) {
        try {
            val call: Call<LocationResponse> = locationService.getLocation()
            val response: Response<LocationResponse> = call.execute()
            if (response.isSuccessful) {
                Thread.sleep(100)
                val locationResponse = response.body()!!
                latitude = locationResponse.lat.toString()
                longitude = locationResponse.lon.toString()
                Timber.i("IP location: lat: $latitude / lon: $longitude")
            }
        } catch (e: Exception) {
            Timber.i("No internet connection")
            _title.postValue(NO_INTERNET)
        } catch (e: SocketTimeoutException) {
            Timber.i("Connection timeout error")
            _title.postValue(BAD_INTERNET)
        }

        gpsLocationTask.addOnSuccessListener {
            if (it != null) {
                latitude = it.latitude.toString()
                longitude = it.longitude.toString()
                Timber.i("GPS location: lat: $latitude / lon: $longitude")
                return@addOnSuccessListener
            }
        }

        loadData()
        _reload.postValue(false)
    }

    fun changeLocation(city: String) {
        Timber.i("Location changed, new city name: $city")
        currentCity = city
        loadData()
    }

    companion object {
        @SuppressLint("SimpleDateFormat")
        val dateFormatTimeStamp = SimpleDateFormat("E dd.MM hh:mm")

        @SuppressLint("SimpleDateFormat")
        val dateFormatDay = SimpleDateFormat("dd")

        const val KELVIN = 272.15
        const val NO_CITY = "city not found"
        const val DEFAULT_CITY = ""
        const val NO_INTERNET = "no internet connection  pull to refresh"
        const val BAD_INTERNET = "bad internet connection"
    }
}