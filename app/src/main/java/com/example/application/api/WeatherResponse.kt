package com.example.application.api

import com.google.gson.annotations.SerializedName


data class WeatherResponse(
    @SerializedName("list")
    val list: ArrayList<WeatherList> = ArrayList(),
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("city")
    val city: City = City(),
    @SerializedName("message")
    val message: String = ""
)

data class City(
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("name")
    val name: String = "",
    @SerializedName("coord")
    val coord: WeatherCoord = WeatherCoord()
)

data class WeatherList(
    @SerializedName("dt")
    val dt: Long,
    @SerializedName("main")
    val main: WeatherMain = WeatherMain(),
    @SerializedName("weather")
    val weather: ArrayList<WeatherToday> = ArrayList()
)

data class WeatherMain(
    @SerializedName("temp")
    val temp: Float = 0.toFloat()
)

data class WeatherToday(
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("description")
    val description: String,
    @SerializedName("icon")
    val icon: String = "10d"
)

data class WeatherCoord(
    @SerializedName("lon")
    val lon: Float = 0.toFloat(),
    @SerializedName("lat")
    val lat: Float = 0.toFloat()
)
