package com.example.application.ViewModels

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.application.*
import com.example.application.api.LocationResponse
import com.example.application.api.LocationService
import com.example.application.api.WeatherService
import com.example.application.models.Weather
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.floor

class WeatherListViewModel(application: Application) : AndroidViewModel(application) {
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

    fun loadData() {
        _reload.postValue(true)
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(WeatherService::class.java)
        val call: Call<WeatherResponse> = service.getCurrentWeatherData(q = currentCity, lat = lat, lon = lon, app_id = APP_ID)

        call.enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                if(response.body() == null){
                    _title.postValue("city not found")
                    _header.postValue("")
                    _reload.postValue(false)
                    return
                }

                val weatherResponse = response.body()!!
                val weather =  mutableListOf<Weather>()
                var lastTime = 0

                _title.postValue(weatherResponse.city.name)
                _header.postValue(floor(weatherResponse.list[0].main.temp - KELVIN).toString() + "°C")
                _headerImageUrl.postValue("https://openweathermap.org/img/wn/${weatherResponse.list[0].weather[0].icon}@4x.png")

                weatherResponse.list.forEach{ weatherItem ->
                    val date = weatherItem.dt?.toLong()?.times(1000)?.let { Date(it) }
                    val time = dateFormatTimeStamp.format(date)
                    val checkTime = dateFormatDay.format(date)

                    if(!lastTime.equals(checkTime.toInt())) {
                        weather.add(
                            weatherResponseToWeather(weatherItem, weatherResponse, time, checkTime)
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

    fun loadLocation(){
        val retrofit = Retrofit.Builder()
            .baseUrl(LOCATION_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(LocationService::class.java)
        val call: Call<LocationResponse> = service.getLocation()

        call.enqueue(object : Callback<LocationResponse> {
            override fun onResponse(call: Call<LocationResponse>, response: Response<LocationResponse>) {
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

    fun changeLocation(city: String){
        currentCity = city
        loadData()
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
        const val RESPONSE_CODE_OK = 200
        const val LOCATION_URL = "http://ip-api.com/json/"
        const val BASE_URL = "https://api.openweathermap.org/"
        const val APP_ID = "c46b6b253436ddd455030408be9b19bf"
        const val KELVIN = 272.15
    }
}