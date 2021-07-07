package com.example.application.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.application.ViewModels.WeatherListViewModel
import com.example.application.api.LocationService
import com.example.application.api.WeatherService
import com.example.application.dependencies.DaggerDaggerComponent


class WeatherListViewModelFactory: ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val locationService = DaggerDaggerComponent.create().getLocation().service
        val weatherService = DaggerDaggerComponent.create().getWeather().service
        return WeatherListViewModel(weatherService, locationService) as T
    }
}