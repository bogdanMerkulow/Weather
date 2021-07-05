package com.example.application

import com.google.gson.annotations.SerializedName


data class WeatherResponse (
    @SerializedName("list")
    var list:ArrayList<WeatherList> = ArrayList<WeatherList>(),
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("city")
    var city: City = City(),
    @SerializedName("message")
    var message: String = ""
)

data class City (
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("name")
    var name: String = "",
    @SerializedName("coord")
    var coord: WeatherCoord? = null
)

data class WeatherList(
    @SerializedName("dt")
    var dt: Int? = null,
    @SerializedName("main")
    var main:WeatherMain = WeatherMain(),
    @SerializedName("weather")
    var weather: ArrayList<WeatherToday> = ArrayList<WeatherToday>()
)

data class WeatherMain(
    @SerializedName("temp")
    var temp: Float = 0.toFloat()
)

data class WeatherToday (
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("description")
    var description: String? = null,
    @SerializedName("icon")
    var icon: String? = "10d"
)

data class WeatherCoord (
    @SerializedName("lon")
    var lon: Float = 0.toFloat(),
    @SerializedName("lat")
    var lat: Float = 0.toFloat()
)
