package com.example.application.dependencies

import com.example.application.list.viewmodels.WeatherListViewModel
import com.example.application.api.LocationService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class LocationClient @Inject constructor() {
    private val retrofit = Retrofit.Builder()
        .baseUrl(WeatherListViewModel.LOCATION_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val service = retrofit.create(LocationService::class.java)
}