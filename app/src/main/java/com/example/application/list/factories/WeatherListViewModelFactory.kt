package com.example.application.list.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.application.list.viewmodels.WeatherListViewModel
import com.example.application.dependencies.DaggerDaggerComponent


class WeatherListViewModelFactory: ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val locationService = DaggerDaggerComponent.create().getLocation().service
        val weatherService = DaggerDaggerComponent.create().getWeather().service
        return WeatherListViewModel(weatherService, locationService) as T
    }
}