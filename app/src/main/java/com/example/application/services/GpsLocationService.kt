package com.example.application.services

import android.app.Application
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class GpsLocationService(
    private val application: Application
) {
    fun getService(): FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(application.applicationContext)
    }
}