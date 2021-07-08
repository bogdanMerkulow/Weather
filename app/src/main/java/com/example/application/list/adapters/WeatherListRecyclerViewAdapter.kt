package com.example.application.list.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.application.models.Weather
import com.example.application.list.viewholders.WeatherListViewHolder

class WeatherListRecyclerViewAdapter(private val listener: (Weather) -> Unit) :
	RecyclerView.Adapter<WeatherListViewHolder>() {

	private var weather: List<Weather> = listOf()

	fun addWeather(weather: List<Weather>){
		this.weather = weather
		notifyDataSetChanged()
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