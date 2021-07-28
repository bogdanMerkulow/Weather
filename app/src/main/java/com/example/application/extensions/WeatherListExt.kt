package com.example.application.extensions

import android.annotation.SuppressLint
import com.example.application.api.WeatherList
import com.example.application.models.Weather
import java.text.SimpleDateFormat
import kotlin.math.ceil

private const val KELVIN = 272.15

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
