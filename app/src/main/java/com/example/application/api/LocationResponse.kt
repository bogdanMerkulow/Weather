package com.example.application.api

import com.google.gson.annotations.SerializedName

class LocationResponse {
    @SerializedName("lat")
    var lat: String? = ""
    @SerializedName("lon")
    var lon: String? = ""
}