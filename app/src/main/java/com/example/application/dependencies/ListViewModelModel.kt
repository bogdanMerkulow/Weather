package com.example.application.dependencies

import com.example.application.api.LocationService
import com.example.application.api.WeatherService
import com.example.application.list.viewmodels.WeatherListViewModel
import dagger.Module
import dagger.Provides

@Module(includes = [WeatherModule::class, LocationModule::class])
class ListViewModelModel {

    @Provides
    fun getViewModel(weatherService: WeatherService, locationService: LocationService): WeatherListViewModel{
        return WeatherListViewModel(weatherService, locationService)
    }
}