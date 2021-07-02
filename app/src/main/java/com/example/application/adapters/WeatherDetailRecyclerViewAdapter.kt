package com.example.application.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.application.R
import com.example.application.models.Weather

class WeatherDetailRecyclerViewAdapter(private val weather: List<Weather>):
	RecyclerView.Adapter<WeatherDetailRecyclerViewAdapter.WeatherDetailViewHolder>() {
	
	class WeatherDetailViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
		var icon: ImageView? = null
		var title: TextView? = null
		var desc: TextView? = null
		var state: TextView? = null
		
		
		init {
			icon = itemView.findViewById(R.id.weather_icon)
			title = itemView.findViewById(R.id.weather_title)
			desc = itemView.findViewById(R.id.weather_desc)
			state = itemView.findViewById(R.id.weather_state)
		}
	}
	
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherDetailViewHolder {
		val itemView = LayoutInflater.from(parent.context).inflate(R.layout.weather_list, parent, false)
		return WeatherDetailViewHolder(itemView)
	}
	
	override fun onBindViewHolder(holder: WeatherDetailViewHolder, position: Int) {
		holder.title?.text = weather[position].title
		holder.desc?.text = weather[position].getTemp()
		holder.state?.text = weather[position].state

		Glide
			.with(holder.itemView)
			.load(weather[position].getIconUrl())
			.into(holder.icon!!)
	}
	
	override fun getItemCount(): Int {
		return weather.size
	}
}