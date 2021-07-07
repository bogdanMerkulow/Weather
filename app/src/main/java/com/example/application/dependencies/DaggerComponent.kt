package com.example.application.dependencies

import dagger.Component

@Component
interface DaggerComponent {
    fun getWeather(): WeatherClient
    fun getLocation(): LocationClient
}