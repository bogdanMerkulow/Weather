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
import com.example.application.models.Coord
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
    private val data: MutableLiveData<MutableList<Weather>> = MutableLiveData<MutableList<Weather>>()
    private val location: MutableLiveData<Coord> = MutableLiveData<Coord>()
    private val reload: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    private val title: MutableLiveData<String> = MutableLiveData<String>()
    private val header: MutableLiveData<String> = MutableLiveData<String>()
    private val headerImageUrl: MutableLiveData<String> = MutableLiveData<String>()
    private val city: MutableLiveData<String> = MutableLiveData<String>()

    fun getData(): LiveData<List<Weather>>{
        return data as LiveData<List<Weather>>
    }

    fun isReload(): LiveData<Boolean>{
        return reload
    }

    fun getTitle(): LiveData<String>{
        return title
    }

    fun getHeader(): LiveData<String>{
        return header
    }

    fun getHeaderImageUrl(): LiveData<String>{
        return headerImageUrl
    }

    fun getCity(): LiveData<String>{
        return city
    }

    fun loadData(q: String, lat: String, lon: String, day: String = "0") {
        reload.postValue(true)
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(WeatherService::class.java)
        val call: Call<WeatherResponse> = service.getCurrentWeatherData(q = q, lat = lat, lon = lon, app_id = APP_ID)

        call.enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                if(response.body() == null){
                    title.postValue("city not found")
                    header.postValue("")
                    reload.postValue(false)
                    return
                }

                val weatherResponse = response.body()!!
                val weather =  mutableListOf<Weather>()
                var lastTime = 0

                city.postValue(weatherResponse.city.name)
                title.postValue(weatherResponse.city.name)
                header.postValue(floor(weatherResponse.list[0].main.temp - KELVIN).toString() + "°C")
                headerImageUrl.postValue("https://openweathermap.org/img/wn/${weatherResponse.list[0].weather[0].icon}@4x.png")

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

                data.postValue(weather)
                reload.postValue(false)
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                title.postValue("no internet connection  pull to refresh")
                reload.postValue(false)
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
                    loadData("", locationResponse.lat.toString(), locationResponse.lon.toString(), "")
                }
            }

            override fun onFailure(call: Call<LocationResponse>, t: Throwable) {
                title.postValue("no internet connection  pull to refresh")
                reload.postValue(false)
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
        const val RESPONSE_CODE_OK = 200
        const val LOCATION_URL = "http://ip-api.com/json/"
        const val BASE_URL = "https://api.openweathermap.org/"
        const val APP_ID = "c46b6b253436ddd455030408be9b19bf"
        const val KELVIN = 272.15
    }
}