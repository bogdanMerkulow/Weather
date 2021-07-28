package com.example.application

import android.app.Application
import com.example.application.dependencies.ApplicationModule
import com.example.application.dependencies.DaggerComponent
import com.example.application.dependencies.DaggerDaggerComponent

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        daggerComponent = DaggerDaggerComponent.builder()
            .applicationModule(ApplicationModule(this))
            .build()
    }

    companion object {
        lateinit var daggerComponent: DaggerComponent
    }
}