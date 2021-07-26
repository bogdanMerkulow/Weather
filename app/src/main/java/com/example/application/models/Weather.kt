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
        fun responseConvert(weatherItem: WeatherList): Weather {
            val date = weatherItem.dt?.toLong()?.times(1000)?.let { Date(it) }
            val time = dateFormatTimeStamp.format(date)
            val checkTime = dateFormatDay.format(date)

            return Weather(
                iconName = weatherItem.weather[0].icon,
                title = time,
                temp = (ceil(weatherItem.main.temp - KELVIN)).toFloat(),
                state = weatherItem.weather[0].description,
                dayNumber = checkTime
            )
        }

        private const val KELVIN = 272.15

        @SuppressLint("SimpleDateFormat")
        val dateFormatTimeStamp = SimpleDateFormat("E dd.MM hh:mm")

        @SuppressLint("SimpleDateFormat")
        val dateFormatDay = SimpleDateFormat("dd")

    }
}