package com.example.application.models

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
}