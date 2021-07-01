package com.example.application

import com.google.gson.annotations.SerializedName


class WeatherResponse {
    @SerializedName("list")
    var list = ArrayList<List_>()
    @SerializedName("id")
    var id: Int = 0
    @SerializedName("city")
    var city: City = City()
    @SerializedName("message")
    var message: String = ""
}

class City{
    @SerializedName("id")
    var id: Int = 0
    @SerializedName("name")
    var name: String = ""
    @SerializedName("coord")
    var coord: Coord? = null
}

class List_{
    @SerializedName("dt")
    var dt: Int? = null
    @SerializedName("main")
    var main = Main_()
    @SerializedName("weather")
    var weather = ArrayList<Weather_>()
}

class Main_{
    @SerializedName("temp")
    var temp: Float = 0.toFloat()
}

class Weather_ {
    @SerializedName("id")
    var id: Int = 0
    @SerializedName("description")
    var description: String? = null
    @SerializedName("icon")
    var icon: String? = "10d"
}

class Coord {
    @SerializedName("lon")
    var lon: Float = 0.toFloat()
    @SerializedName("lat")
    var lat: Float = 0.toFloat()
}
