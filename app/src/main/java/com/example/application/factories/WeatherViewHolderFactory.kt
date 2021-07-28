package com.example.application.factories

import android.view.View
import com.example.application.adapters.ViewHolder
import com.example.application.adapters.ViewHolderFactory
import com.example.application.viewholders.WeatherViewHolder

class WeatherViewHolderFactory<T> : ViewHolderFactory<T> {
    override fun create(view: View): ViewHolder<T> {
        return WeatherViewHolder(view) as ViewHolder<T>
    }
}