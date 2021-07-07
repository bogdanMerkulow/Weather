package com.example.application.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.application.ViewModels.WeatherDetailViewModel
import com.example.application.api.WeatherService
import com.example.application.dependencies.DaggerDaggerComponent

class WeatherDetailViewModelFactory: ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            val weatherService = DaggerDaggerComponent.create().getWeather().service
            return WeatherDetailViewModel(weatherService) as T
        }
    }