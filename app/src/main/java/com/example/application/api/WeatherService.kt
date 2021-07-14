package com.example.application.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET("data/2.5/forecast?")
    fun getCurrentWeatherData(
        @Query("q") q: String = "",
        @Query("lat") lat: String = "",
        @Query("lon") lon: String = "",
        @Query("APPID") app_id: String,
        @Query("cnt") cnt: String = "35",
        @Query("lang") lang: String = "ru"
    ): Call<WeatherResponse>
}
