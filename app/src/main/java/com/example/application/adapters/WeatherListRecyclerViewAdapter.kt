package com.example.application.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.application.R
import com.example.application.models.Weather
import com.example.application.viewholders.WeatherListViewHolder

class WeatherListRecyclerViewAdapter(private val listener: Listener) :
	RecyclerView.Adapter<WeatherListViewHolder>() {

	private var weather: MutableList<Weather> = mutableListOf()

	interface Listener{
		fun onItemClick(weather: Weather)
	}

	fun addWeather(weather: MutableList<Weather>){
		this.weather = weather
	}
	
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherListViewHolder {
		val inflater = LayoutInflater.from(parent.context)
		return WeatherListViewHolder(inflater, parent, listener)
	}

	override fun onBindViewHolder(holder: WeatherListViewHolder, position: Int) {
		holder.bindWeather(weather[position])
	}
	
	override fun getItemCount(): Int {
		return weather.size
	}
}