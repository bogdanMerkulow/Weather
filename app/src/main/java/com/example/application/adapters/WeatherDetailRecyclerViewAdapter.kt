package com.example.application.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.application.models.Weather
import com.example.application.viewholders.WeatherDetailViewHolder

class WeatherDetailRecyclerViewAdapter:
	RecyclerView.Adapter<WeatherDetailViewHolder>() {

	private var weather: List<Weather> = listOf()

	fun addWeather(weather: List<Weather>){
		this.weather = weather
		notifyDataSetChanged()
	}
	
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherDetailViewHolder {
		val inflater = LayoutInflater.from(parent.context)
		return WeatherDetailViewHolder(inflater, parent)
	}
	
	override fun onBindViewHolder(holder: WeatherDetailViewHolder, position: Int) {
		holder.bindWeather(weather[position])
	}
	
	override fun getItemCount(): Int {
		return weather.size
	}
}