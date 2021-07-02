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

class WeatherListRecyclerViewAdapter(private val weather: List<Weather>, private val listener: Listener) :
	RecyclerView.Adapter<WeatherListRecyclerViewAdapter.WeatherViewHolder>() {
	
	
	interface Listener{
		fun onItemClick(weather: Weather)
	}
	
	class WeatherViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
		var icon: ImageView? = null
		var title: TextView? = null
		var desc: TextView? = null
		var state: TextView? = null
		var progress: ProgressBar? = null
		
		
		init {
			icon = itemView.findViewById(R.id.weather_icon)
			title = itemView.findViewById(R.id.weather_title)
			desc = itemView.findViewById(R.id.weather_desc)
			state = itemView.findViewById(R.id.weather_state)
			progress = itemView.findViewById(R.id.progress_circular)
		}
	}
	
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
		val itemView = LayoutInflater.from(parent.context).inflate(R.layout.weather_list, parent, false)
		return WeatherViewHolder(itemView)
	}
	
	override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
		holder.title?.text = weather[position].title
		holder.desc?.text = weather[position].getTemp()
		holder.state?.text = weather[position].state
		holder.itemView.setOnClickListener {
			listener.onItemClick(weather[position])
		}

		Glide
			.with(holder.itemView)
			.load(weather[position].getIconUrl())
			.into(holder.icon!!)
	}
	
	override fun getItemCount(): Int {
		return weather.size
	}
}