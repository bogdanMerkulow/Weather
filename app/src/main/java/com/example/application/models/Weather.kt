package com.example.application.models

import android.annotation.SuppressLint
import com.example.application.api.WeatherList
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.ceil

data class Weather(
    val iconName: String? = "",
    val title: String? = "",
    val temp: Float = 0.0f,
    val state: String? = "",
    val city: String? = "",
    private val lat: String = "",
    private val lon: String = "",
    val dayNumber: String = ""
) {

    fun getIconUrl(): String {
        return "https://openweathermap.org/img/wn/${iconName}@4x.png"
    }

    fun getTemp(): String {
        if (temp.toInt() > 30) {
            return "${temp.toInt()}\uD83D\uDD25"
        }
        return "${temp.toInt()}°C"
    }

    companion object {
        fun WeatherList.toWeather(timeFormat: String = "E dd.MM hh:mm"): Weather {
            val dateFormatTimeStamp = SimpleDateFormat(timeFormat)
            val unixTimestamp = this.dt?.toLong()?.times(1000)?.let { Date(it) }
            val timestamp = dateFormatTimeStamp.format(unixTimestamp)
            val dayNumber = dateFormatDay.format(unixTimestamp)

            return Weather(
                iconName = this.weather[0].icon,
                title = timestamp,
                temp = (ceil(this.main.temp - KELVIN)).toFloat(),
                state = this.weather[0].description,
                dayNumber = dayNumber
            )
        }

        private const val KELVIN = 272.15

        @SuppressLint("SimpleDateFormat")
        val dateFormatDay = SimpleDateFormat("dd")

    }
}