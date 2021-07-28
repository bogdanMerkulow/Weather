package com.example.application.dependencies

import android.app.Activity
import android.app.Application
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.app.ActivityCompat
import com.example.application.services.GpsLocationService
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import dagger.Module
import dagger.Provides

@Module(includes = [ApplicationModule::class])
class GPSLocationModule {

    @Provides
    fun getLocation(application: Application): Task<Location> {
        val fusedLocationProviderClient = GpsLocationService(application)

        if (ActivityCompat.checkSelfPermission(
                application.applicationContext,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                application as Activity,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                101
            )
        }

        return fusedLocationProviderClient.getService().lastLocation
    }
}