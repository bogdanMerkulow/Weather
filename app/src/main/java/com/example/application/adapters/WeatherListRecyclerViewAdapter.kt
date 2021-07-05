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

class WeatherListRecyclerViewAdapter(private val listener: Listener) :
	RecyclerView.Adapter<WeatherListRecyclerViewAdapter.WeatherViewHolder>() {

	private var weather: MutableList<Weather> = mutableListOf()

	interface Listener{
		fun onItemClick(weather: Weather)
	}
	
	class WeatherViewHolder(inflater: LayoutInflater, parent: ViewGroup, private val listener: Listener):
		RecyclerView.ViewHolder(inflater.inflate(R.layout.weather_list, parent, false)) {

		private var icon: ImageView = itemView.findViewById(R.id.weather_icon)
		private var title: TextView = itemView.findViewById(R.id.weather_title)
		private var desc: TextView = itemView.findViewById(R.id.weather_desc)
		private var state: TextView = itemView.findViewById(R.id.weather_state)

		fun bindWeather(weather: Weather){
			title.text = weather.title
			desc.text = weather.getTemp()
			state.text = weather.state
			itemView.setOnClickListener {
				this.listener.onItemClick(weather)
			}

			Glide
				.with(itemView)
				.load(weather.getIconUrl())
				.into(icon)
		}
	}

	fun addWeather(weather: MutableList<Weather>){
		this.weather = weather
	}
	
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
		val inflater = LayoutInflater.from(parent.context)
		return WeatherViewHolder(inflater, parent, listener)
	}

	override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
		holder.bindWeather(weather[position])
	}
	
	override fun getItemCount(): Int {
		return weather.size
	}
}