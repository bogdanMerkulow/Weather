package com.example.application

import com.google.gson.annotations.SerializedName


data class WeatherResponse(
    @SerializedName("list")
    val list: ArrayList<WeatherList> = ArrayList<WeatherList>(),
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
    val coord: WeatherCoord? = null
)

data class WeatherList(
    @SerializedName("dt")
    val dt: Int? = null,
    @SerializedName("main")
    val main: WeatherMain = WeatherMain(),
    @SerializedName("weather")
    val weather: ArrayList<WeatherToday> = ArrayList<WeatherToday>()
)

data class WeatherMain(
    @SerializedName("temp")
    val temp: Float = 0.toFloat()
)

data class WeatherToday(
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("icon")
    val icon: String? = "10d"
)

data class WeatherCoord(
    @SerializedName("lon")
    val lon: Float = 0.toFloat(),
    @SerializedName("lat")
    val lat: Float = 0.toFloat()
)
