package com.example.application.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.application.dependencies.DaggerDaggerComponent

class ViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val viewModel = DaggerDaggerComponent.create().getViewModelsMap()
        return viewModel[modelClass] as T
    }
}