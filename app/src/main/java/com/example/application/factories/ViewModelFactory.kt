package com.example.application.factories

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.application.dependencies.ActivityModule
import com.example.application.dependencies.DaggerDaggerComponent

class ViewModelFactory(private val activity: Activity?) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val daggerComponent = DaggerDaggerComponent.builder()
            .activityModule(ActivityModule(activity!!))
            .build()

        val viewModel = daggerComponent.getViewModelsMap()
        return viewModel[modelClass] as T
    }
}