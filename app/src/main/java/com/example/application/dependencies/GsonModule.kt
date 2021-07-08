package com.example.application.dependencies

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import retrofit2.converter.gson.GsonConverterFactory

@Module
class GsonModule {

    @Provides
    fun gson(): Gson{
        return GsonBuilder().create()
    }

    @Provides
    fun gsonConverterFactory(gson: Gson): GsonConverterFactory{
        return GsonConverterFactory.create(gson)
    }
}