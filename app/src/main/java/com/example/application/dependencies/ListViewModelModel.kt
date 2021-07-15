package com.example.application.dependencies

import android.location.Location
import androidx.lifecycle.ViewModel
import com.example.application.api.LocationService
import com.example.application.api.WeatherService
import com.example.application.list.viewmodels.WeatherListViewModel
import com.google.android.gms.tasks.Task
import dagger.Module
import dagger.Provides
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap

@Module(includes = [WeatherModule::class, LocationModule::class, GPSLocationModule::class])
class ListViewModelModel {

    @IntoMap
    @ClassKey(WeatherListViewModel::class)
    @Provides
    fun getViewModel(
        weatherService: WeatherService,
        ipLocationService: LocationService,
        GPSLocationTask: Task<Location>
    ): ViewModel {
        return WeatherListViewModel(weatherService, ipLocationService, GPSLocationTask)
    }
}