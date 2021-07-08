package com.example.application.dependencies

import com.example.application.api.LocationService
import com.example.application.list.viewmodels.WeatherListViewModel
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named

@Module(includes = [GsonModule::class])
class LocationModule {

    @Provides
    fun locationService(@Named("location") retrofit: Retrofit): LocationService {
        return retrofit.create(LocationService::class.java)
    }

    @Named("location")
    @Provides
    fun retrofit(gsonConverterFactory: GsonConverterFactory): Retrofit{
        return Retrofit.Builder()
            .baseUrl(WeatherListViewModel.LOCATION_URL)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }
}