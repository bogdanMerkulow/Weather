package com.example.application.dependencies

import com.example.application.ViewModels.WeatherListViewModel
import com.example.application.api.WeatherService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class WeatherClient @Inject constructor(){
    private val retrofit = Retrofit.Builder()
        .baseUrl(WeatherListViewModel.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val service = retrofit.create(WeatherService::class.java)
}