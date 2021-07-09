package com.example.application.factories

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.application.R
import com.example.application.viewholders.WeatherViewHolder

class ViewHolderFactory {
    fun create(view: View, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.weather_list -> WeatherViewHolder(view)
            else -> throw Exception("Wrong view type")
        }
    }
}