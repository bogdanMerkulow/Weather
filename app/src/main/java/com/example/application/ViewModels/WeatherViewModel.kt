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

class WeatherViewModel(application: Application) : AndroidViewModel(application) {
    private val data: MutableLiveData<MutableList<Weather>> = MutableLiveData<MutableList<Weather>>()
    private val location: MutableLiveData<Coord> = MutableLiveData<Coord>()
    private val reload: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    private val error: MutableLiveData<Boolean> = MutableLiveData<Boolean>()

    fun getData(): LiveData<List<Weather>>{
        return data as LiveData<List<Weather>>
    }

    fun getLocation(): LiveData<Coord>{
        return location
    }

    fun isReload(): LiveData<Boolean>{
        return reload
    }

    fun getError(): LiveData<Boolean>{
        return error
    }

    fun loadData(q: String, lat: String, lon: String, detail: Boolean, day: String = "0") {
        reload.postValue(true)
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(WeatherService::class.java)
        val call: Call<WeatherResponse> = service.getCurrentWeatherData(q = q, lat = lat, lon = lon, app_id = APP_ID)

        call.enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                val weather = weatherResponseToWeather(response.body()!!, detail, day)
                data.postValue(weather)
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                error.postValue(true)
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
                    location.postValue(Coord(locationResponse.lat.toString(), locationResponse.lon.toString()))
                }
            }

            override fun onFailure(call: Call<LocationResponse>, t: Throwable) {
                error.postValue(true)
            }
        })
    }

    private fun weatherResponseToWeather(weatherResponse: WeatherResponse, detail: Boolean, day: String): MutableList<Weather>{
        val weather =  mutableListOf<Weather>()
        var lastTime = 0
        weatherResponse.list.forEach{ list ->
            val date = list.dt?.toLong()?.times(1000)?.let { Date(it) }
            val time = dateFormatTimeStamp.format(date)
            val checkTime = dateFormatDay.format(date)

            val filterVariant: Boolean = if(detail){
                day.equals(checkTime)
            }else{
                !lastTime.equals(checkTime.toInt())
            }

            if(filterVariant) {
                weather.add(
                    Weather(
                        iconName = list.weather[0].icon,
                        title = time,
                        temp = (floor(list.main.temp - KELVIN)).toFloat(),
                        state = list.weather[0].description,
                        city = weatherResponse.city.name,
                        lat = weatherResponse.city.coord?.lat.toString(),
                        lon = weatherResponse.city.coord?.lon.toString(),
                        dayNumber = checkTime
                    )
                )

            }
            lastTime = checkTime.toInt()
        }
        return weather
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