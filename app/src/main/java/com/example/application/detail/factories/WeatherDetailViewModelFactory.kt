package com.example.application.detail.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.application.dependencies.DaggerDaggerComponent

class WeatherDetailViewModelFactory: ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val viewModel = DaggerDaggerComponent.create().getDetailViewModel()
        return viewModel as T
    }
}