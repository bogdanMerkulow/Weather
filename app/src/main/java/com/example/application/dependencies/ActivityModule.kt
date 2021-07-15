package com.example.application.dependencies

import android.app.Activity
import dagger.Module
import dagger.Provides

@Module
class ActivityModule(private val activity: Activity) {

    @Provides
    fun getActivity(): Activity {
        return activity
    }
}