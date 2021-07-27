package com.example.application.models

import android.annotation.SuppressLint
import com.example.application.api.WeatherList
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.ceil

data class Weather(
    private val iconName: String = "",
    val title: String = "",
    private val temp: Float = 0.0f,
    val state: String = "",
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
            @SuppressLint("SimpleDateFormat")
            val dateFormatTimeStamp = SimpleDateFormat(timeFormat)

            @SuppressLint("SimpleDateFormat")
            val dateFormatDay = SimpleDateFormat("dd")
            val unixTimestamp = this.dt.times(1000)
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
    }
}