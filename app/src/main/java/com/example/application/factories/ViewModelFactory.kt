package com.example.application.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.application.dependencies.DaggerDaggerComponent
import com.example.application.detail.viewmodels.WeatherDetailViewModel
import com.example.application.list.viewmodels.WeatherListViewModel
import java.lang.IllegalArgumentException

class ViewModelFactory: ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when (modelClass) {
            WeatherListViewModel::class.java -> DaggerDaggerComponent.create().getListViewModel() as T
            WeatherDetailViewModel::class.java -> DaggerDaggerComponent.create().getDetailViewModel() as T
            else -> throw IllegalArgumentException("${modelClass.canonicalName} not have factory")
        }
    }
}