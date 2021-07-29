package com.example.application.api

import com.example.application.BuildConfig
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET("data/2.5/forecast?")
    suspend fun getCurrentWeatherData(
        @Query("q") city: String = "",
        @Query("lat") latitude: String = "",
        @Query("lon") longitude: String = "",
        @Query("APPID") api_key: String = BuildConfig.OWM_API_KEY,
        @Query("cnt") count: String = "35",
        @Query("lang") lang: String = "ru"
    ): WeatherResponse
}
