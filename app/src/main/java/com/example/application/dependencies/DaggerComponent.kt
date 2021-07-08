package com.example.application.dependencies

import com.example.application.detail.viewmodels.WeatherDetailViewModel
import com.example.application.list.viewmodels.WeatherListViewModel
import dagger.Component

@Component(modules = [DetailViewModelModel::class, ListViewModelModel::class])
interface DaggerComponent {
    fun getDetailViewModel(): WeatherDetailViewModel
    fun getListViewModel(): WeatherListViewModel
}