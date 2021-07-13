package com.example.application.dependencies

import androidx.lifecycle.ViewModel
import com.example.application.api.LocationService
import com.example.application.api.WeatherService
import com.example.application.list.viewmodels.WeatherListViewModel
import dagger.Module
import dagger.Provides
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap
import dagger.multibindings.IntoSet
import dagger.multibindings.StringKey
import javax.inject.Named

@Module(includes = [WeatherModule::class, LocationModule::class])
class ListViewModelModel {

    @IntoMap
    @ClassKey(WeatherListViewModel::class)
    @Provides
    fun getViewModel(weatherService: WeatherService, locationService: LocationService): ViewModel{
        return WeatherListViewModel(weatherService, locationService)
    }
}