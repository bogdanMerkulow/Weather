package com.example.application.dependencies

import androidx.lifecycle.ViewModel
import com.example.application.MainActivity
import com.example.application.detail.viewmodels.WeatherDetailViewModel
import com.example.application.list.viewmodels.WeatherListViewModel
import dagger.Component
import dagger.Module
import dagger.multibindings.Multibinds
import javax.inject.Inject
import javax.inject.Named

@Component(modules = [DetailViewModelModel::class, ListViewModelModel::class])
interface DaggerComponent {
    fun getViewModelsMap(): Map<Class<*>, ViewModel>
}