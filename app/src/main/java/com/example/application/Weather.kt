package com.example.application

data class Weather(
	private var iconName: String? = "",
	private val title: String? = "",
	private val temp: Float = 0.0f,
	private val state: String? = "",
	private val city: String? = "",
	private val lat: String = "",
	private val lon: String = "",
	private val dayNumber: String = "",
	private var wrongCity: Boolean = false
) {
	fun getCoords() : Array<String>{
		return arrayOf(lat, lon)
	}

	fun getCity(): String?{
		return city
	}

	fun getIconUrl(): String? {
		return "https://openweathermap.org/img/wn/${iconName}@4x.png"
	}

	fun getTitle(): String? {
		return title
	}

	fun getTemp(): String {
		return "${temp.toInt()}°C"
	}

	fun getState(): String? {
		return state
	}

	fun getDayNumber(): String {
		return dayNumber
	}

	fun getError(): Boolean{
		return wrongCity
	}
}