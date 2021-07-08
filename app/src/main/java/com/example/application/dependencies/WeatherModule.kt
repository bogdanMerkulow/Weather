package com.example.application.dependencies

import com.example.application.api.WeatherService
import com.example.application.list.viewmodels.WeatherListViewModel
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named

@Module(includes = [GsonModule::class])
class WeatherModule {

    @Provides
    fun weatherService(@Named("weather") retrofit: Retrofit): WeatherService {
        return retrofit.create(WeatherService::class.java)
    }

    @Named("weather")
    @Provides
    fun retrofit(gsonConverterFactory: GsonConverterFactory): Retrofit{
        return Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/")
            .addConverterFactory(gsonConverterFactory)
            .build()
    }
}