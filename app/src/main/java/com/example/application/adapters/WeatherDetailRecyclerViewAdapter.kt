package com.example.application.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.application.R
import com.example.application.models.Weather

class WeatherDetailRecyclerViewAdapter:
	RecyclerView.Adapter<WeatherDetailRecyclerViewAdapter.WeatherDetailViewHolder>() {

	private var weather: MutableList<Weather> = mutableListOf()

	class WeatherDetailViewHolder(inflater: LayoutInflater, parent: ViewGroup):
		RecyclerView.ViewHolder(inflater.inflate(R.layout.weather_list, parent, false)) {

		private var icon: ImageView? = null
		private var title: TextView? = null
		private var desc: TextView? = null
		private var state: TextView? = null

		init {
			icon = itemView.findViewById(R.id.weather_icon)
			title = itemView.findViewById(R.id.weather_title)
			desc = itemView.findViewById(R.id.weather_desc)
			state = itemView.findViewById(R.id.weather_state)
		}

		fun bindWeather(weather: Weather){
			title?.text = weather.title
			desc?.text = weather.getTemp()
			state?.text = weather.state

			Glide
				.with(itemView)
				.load(weather.getIconUrl())
				.into(icon!!)
		}
	}

	fun addWeather(weather: MutableList<Weather>){
		this.weather = weather
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