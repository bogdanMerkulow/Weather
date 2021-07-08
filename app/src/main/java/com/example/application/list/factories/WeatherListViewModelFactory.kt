package com.example.application.list.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.application.dependencies.DaggerDaggerComponent


class WeatherListViewModelFactory: ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val viewModel = DaggerDaggerComponent.create().getListViewModel()
        return viewModel as T
    }
}