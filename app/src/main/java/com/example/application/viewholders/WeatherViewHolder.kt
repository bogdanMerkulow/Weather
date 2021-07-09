package com.example.application.viewholders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.application.R
import com.example.application.adapters.WeatherRecyclerViewAdapter
import com.example.application.models.Weather
import com.example.application.models.OnItemClickListener

class WeatherViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), WeatherRecyclerViewAdapter.Binder<Weather>{

    private val icon: ImageView = itemView.findViewById(R.id.weather_icon)
    private val title: TextView = itemView.findViewById(R.id.weather_title)
    private val desc: TextView = itemView.findViewById(R.id.weather_desc)
    private val state: TextView = itemView.findViewById(R.id.weather_state)

    override fun bind(data: Weather, listener: OnItemClickListener<Weather>?) {
        itemView.apply {
            title.text = data.title
            desc.text = data.getTemp()
            state.text = data.state
            setOnClickListener{
                listener?.onItemClick(data)
            }

            Glide
                .with(itemView)
                .load(data.getIconUrl())
                .into(icon)

        }
    }
}