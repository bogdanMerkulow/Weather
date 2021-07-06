package com.example.application.viewholders

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.application.R
import com.example.application.adapters.WeatherListRecyclerViewAdapter
import com.example.application.models.Weather

class WeatherListViewHolder(inflater: LayoutInflater, parent: ViewGroup, private val listener: (Weather) -> Unit):
    RecyclerView.ViewHolder(inflater.inflate(R.layout.weather_list, parent, false)) {

    private val icon: ImageView = itemView.findViewById(R.id.weather_icon)
    private val title: TextView = itemView.findViewById(R.id.weather_title)
    private val desc: TextView = itemView.findViewById(R.id.weather_desc)
    private val state: TextView = itemView.findViewById(R.id.weather_state)

    fun bindWeather(weather: Weather){
        title.text = weather.title
        desc.text = weather.getTemp()
        state.text = weather.state
        itemView.setOnClickListener {
            this.listener(weather)
        }

        Glide
            .with(itemView)
            .load(weather.getIconUrl())
            .into(icon)
    }
}