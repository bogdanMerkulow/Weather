package com.example.application.models

import com.example.application.WeatherList
import com.example.application.WeatherResponse
import kotlin.math.ceil

data class Weather(
    val iconName: String? = "",
    val title: String? = "",
    val temp: Float = 0.0f,
    val state: String? = "",
    val city: String? = "",
    private val lat: String = "",
    private val lon: String = "",
    val dayNumber: String = "",
    val wrongCity: Boolean = false
) {

    fun getIconUrl(): String {
        return "https://openweathermap.org/img/wn/${iconName}@4x.png"
    }

    fun getTemp(): String {
        return "${temp.toInt()}°C"
    }

    companion object {
        fun responseConvert(
            weatherItem: WeatherList,
            weatherResponse: WeatherResponse,
            time: String,
            checkTime: String
        ): Weather {
            return Weather(
                iconName = weatherItem.weather[0].icon,
                title = time,
                temp = (ceil(weatherItem.main.temp - KELVIN)).toFloat(),
                state = weatherItem.weather[0].description,
                city = weatherResponse.city.name,
                lat = weatherResponse.city.coord?.lat.toString(),
                lon = weatherResponse.city.coord?.lon.toString(),
                dayNumber = checkTime
            )
        }

        private const val KELVIN = 272.15
    }
}