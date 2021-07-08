package com.example.application.dependencies

import com.example.application.api.WeatherService
import com.example.application.detail.viewmodels.WeatherDetailViewModel
import dagger.Module
import dagger.Provides

@Module(includes = [WeatherModule::class])
class DetailViewModelModel {

    @Provides
    fun getViewModel(weatherService: WeatherService): WeatherDetailViewModel{
        return WeatherDetailViewModel(weatherService)
    }
}