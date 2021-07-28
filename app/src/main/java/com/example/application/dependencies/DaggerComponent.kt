package com.example.application.dependencies

import androidx.lifecycle.ViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [DetailViewModelModel::class, ListViewModelModel::class])
interface DaggerComponent {
    fun getViewModelsMap(): Map<Class<*>, ViewModel>
}