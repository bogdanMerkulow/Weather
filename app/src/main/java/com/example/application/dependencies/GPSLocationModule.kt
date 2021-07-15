package com.example.application.dependencies

import android.app.Activity
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import dagger.Module
import dagger.Provides

@Module(includes = [ActivityModule::class])
class GPSLocationModule {

    @Provides
    fun getLocation(activity: Activity): Task<Location> {
        val fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(activity.applicationContext)

        if (ActivityCompat.checkSelfPermission(
                activity.applicationContext,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                101
            )
        }
        return fusedLocationProviderClient.lastLocation
    }
}