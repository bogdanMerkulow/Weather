package com.example.application.api

import retrofit2.Call
import retrofit2.http.GET

interface LocationService {
    @GET("?234")
    suspend fun getLocation(): LocationResponse
}