package com.example.application.list.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.application.list.viewmodels.WeatherListViewModel
import com.example.application.dependencies.DaggerDaggerComponent


class WeatherListViewModelFactory: ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val daggerComponent = DaggerDaggerComponent.create()
        val locationService = daggerComponent.getLocationService()
        val weatherService = daggerComponent.getWeatherService()
        return WeatherListViewModel(weatherService, locationService) as T
    }
}