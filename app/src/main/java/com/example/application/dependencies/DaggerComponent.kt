package com.example.application.dependencies

import com.example.application.api.LocationService
import com.example.application.api.WeatherService
import dagger.Component
import javax.inject.Named

@Component(modules = [WeatherModule::class, LocationModule::class])
interface DaggerComponent {
    fun getWeatherService(): WeatherService
    fun getLocationService(): LocationService
}