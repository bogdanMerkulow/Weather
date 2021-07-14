package com.example.application.dependencies

import androidx.lifecycle.ViewModel
import com.example.application.api.WeatherService
import com.example.application.detail.viewmodels.WeatherDetailViewModel
import dagger.Module
import dagger.Provides
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap

@Module(includes = [WeatherModule::class])
class DetailViewModelModel {

    @IntoMap
    @ClassKey(WeatherDetailViewModel::class)
    @Provides
    fun getViewModel(weatherService: WeatherService): ViewModel {
        return WeatherDetailViewModel(weatherService)
    }
}