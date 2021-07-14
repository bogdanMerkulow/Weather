package com.example.application.api

import com.google.gson.annotations.SerializedName

data class LocationResponse(
    @SerializedName("lat")
    val lat: String? = "",
    @SerializedName("lon")
    val lon: String? = ""
)