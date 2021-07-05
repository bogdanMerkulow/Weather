package com.example.application.models

data class Weather(
	val iconName: String? = "",
	val title: String? = "",
	val temp: Float = 0.0f,
	val state: String? = "",
	val city: String? = "",
	private val lat: String = "",
	private val lon: String = "",
	val dayNumber: String = "",
	var wrongCity: Boolean = false
) {
	fun getCoords() : Coord {
		return Coord(lat = lat, lon = lon)
	}

	fun getIconUrl(): String {
		return "https://openweathermap.org/img/wn/${iconName}@4x.png"
	}

	fun getTemp(): String {
		return "${temp.toInt()}°C"
	}
}