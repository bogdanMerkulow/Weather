package com.example.application

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.floor

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "NAME_SHADOWING")
class WeatherViewModel(application: Application) : AndroidViewModel(application) {
    val data: MutableLiveData<MutableList<Weather>> = MutableLiveData<MutableList<Weather>>()
        get() = field
    val location: MutableLiveData<MutableList<String>> = MutableLiveData<MutableList<String>>()
        get() = field
    var reload: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
        get() = field

    fun loadData(q: String, lat: String, lon: String, detail: Boolean, day: String = "0") {
        reload.postValue(true)
        val retrofit = Retrofit.Builder()
            .baseUrl(BaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(WeatherService::class.java)
        val call: Call<WeatherResponse> = service.getCurrentWeatherData(q = q, lat = lat, lon = lon, app_id = AppId)

        call.enqueue(object : Callback<WeatherResponse> {
            @SuppressLint("SimpleDateFormat")
            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                if (response.code() == 200) {
                    val weatherResponse = response.body()!!
                    val weather =  mutableListOf<Weather>()
                    var lastTime = 0
                    for(i in 0 until 35){
                        val sdf = SimpleDateFormat("E dd.MM hh:mm")
                        val sdfCheck = SimpleDateFormat("dd")
                        val date = weatherResponse.list[i].dt?.toLong()?.times(1000)?.let { Date(it) }
                        val time = sdf.format(date)
                        val checkTime = sdfCheck.format(date)

                        val filterVariant: Boolean = if(detail){
                            day.equals(checkTime)
                        }else{
                            !lastTime.equals(checkTime.toInt())
                        }

                        if(filterVariant) {
                            weather.add(
                                Weather(
                                    iconName = weatherResponse.list[i].weather[0].icon,
                                    title = time,
                                    temp = (floor(weatherResponse.list[i].main.temp - 272.15)).toFloat(),
                                    state = weatherResponse.list[i].weather[0].description,
                                    city = weatherResponse.city.name,
                                    lat = weatherResponse.city.coord?.lat.toString(),
                                    lon = weatherResponse.city.coord?.lon.toString(),
                                    dayNumber = checkTime
                                )
                            )

                        }
                        lastTime = checkTime.toInt()
                    }
                    data.postValue(weather)
                }else{
                    val weather =  mutableListOf<Weather>()
                    weather.add(
                        Weather(
                            wrongCity = true
                        )
                    )
                    data.postValue(weather)
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                Log.e("failure", t.message.toString())
            }
        })
    }

    fun loadLocation(){
        val retrofit = Retrofit.Builder()
            .baseUrl(locationUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(WeatherService::class.java)
        val call: Call<LocationResponse> = service.getLocation()

        call.enqueue(object : Callback<LocationResponse> {
            override fun onResponse(call: Call<LocationResponse>, response: Response<LocationResponse>) {
                if (response.code() == 200) {
                    val locationResponse = response.body()!!
                    location?.postValue(mutableListOf(locationResponse.lat.toString(), locationResponse.lon.toString()))
                }
            }

            override fun onFailure(call: Call<LocationResponse>, t: Throwable) {
                Log.e("failure", t.message.toString())
            }
        })
    }

    companion object {
        const val locationUrl = "http://ip-api.com/json/"
        const val BaseUrl = "https://api.openweathermap.org/"
        const val AppId = "c46b6b253436ddd455030408be9b19bf"
    }
}