package com.example.application.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.example.application.*
import com.example.application.ViewModels.WeatherViewModel
import com.example.application.adapters.WeatherListRecyclerViewAdapter
import com.example.application.models.Coord
import com.example.application.models.Weather

class WeatherListFragment : Fragment(), WeatherListRecyclerViewAdapter.Listener {
	private lateinit var adapter: WeatherListRecyclerViewAdapter
	private lateinit var viewModel: WeatherViewModel
	private var city: String = ""
	private var title: String? = null


	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		viewModel = ViewModelProvider(this)[WeatherViewModel::class.java]
		adapter = WeatherListRecyclerViewAdapter(this)

		val city = arguments?.getString(WeatherDetailFragment.CITY).toString()
		if(!city.equals("null"))
			this.city = city
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		val rootView = inflater.inflate(R.layout.fragment_weather_list, container, false)
		val rcWeatherList: RecyclerView = rootView.findViewById(R.id.rv_weather_list)
		val progress: ProgressBar = rootView.findViewById(R.id.progress_circular)
		val headerText: TextView = rootView.findViewById(R.id.header_text)
		val imageAnimation = rootView.findViewById<ImageView>(R.id.header_image_animation)
		val headerImage: ImageView = rootView.findViewById(R.id.header_image)
		val changeCityButton = rootView.findViewById<Button>(R.id.change_city)
		val fragmentContainer = rootView.findViewById<SwipeRefreshLayout>(R.id.swipe_refresh)

		rcWeatherList.layoutManager = LinearLayoutManager(activity)
		rcWeatherList.adapter = adapter

		fragmentContainer.setOnRefreshListener{
			onRefreshData(fragmentContainer)
		}

		changeCityButton.setOnClickListener{
			onClickChangeCityButton(inflater)
		}

		viewModel.getError().observe(viewLifecycleOwner){
			activity?.title = "no internet connection  pull to refresh"
		}

		viewModel.isReload().observe(viewLifecycleOwner){ reload ->
			onLiveDataChangeReload(reload, progress)
		}

		viewModel.getLocation().observe(viewLifecycleOwner) { location ->
			viewModel.loadData(this.city, location.lat, location.lon, false, "")
		}

		viewModel.getData().observe(viewLifecycleOwner) { data ->
			onLiveDataChangeData(data, headerText, imageAnimation, headerImage)
		}

		viewModel.loadLocation()

		return rootView
	}

	private fun onClickChangeCityButton(inflater: LayoutInflater) {
		val builder = AlertDialog.Builder(activity)
		builder.setTitle("change city")
		val dialogLayout = inflater.inflate(R.layout.fragment_city_dialog, null)
		val editText  = dialogLayout.findViewById<EditText>(R.id.city_edit_text)
		builder.setView(dialogLayout)
		builder.setPositiveButton("enter") { _, _ ->
			viewModel.loadData(editText.text.toString(), "", "", false, "")
		}
		builder.show()
	}

	private fun onLiveDataChangeData(
		data: List<Weather>,
		headerText: TextView,
		imageAnimation: ImageView,
		headerImage: ImageView
	) {
		if (data[0].wrongCity) {
			headerText.text = ""
			activity?.title = "city not found"
			return
		}

		val headerIconUrl = data[0].getIconUrl()
		imageAnimation.visibility = View.GONE
		imageAnimation.clearAnimation()
		if (headerIconUrl in animWeather) {
			val animationRotateCenter: Animation = AnimationUtils.loadAnimation(
				activity, R.anim.gray_spinner_png
			)
			imageAnimation.visibility = View.VISIBLE
			imageAnimation.startAnimation(animationRotateCenter)
		}

		Glide
			.with(this)
			.load(headerIconUrl)
			.into(headerImage)

		headerText.text = data[0].getTemp()

		adapter.addWeather(data)

		activity?.title = data[0].city
		title = data[0].city
		this.city = data[0].city.toString()
	}

	private fun onLiveDataChangeReload(reload: Boolean, progress: ProgressBar) {
		if(reload){
			progress.visibility = View.VISIBLE
		}else{
			progress.visibility = View.INVISIBLE
		}
	}

	override fun onItemClick(weather: Weather) {
		val transaction = activity?.supportFragmentManager?.beginTransaction()
		val fragment = WeatherDetailFragment()
		val bundle = Bundle()
		bundle.putString(WeatherDetailFragment.CITY, this.city)
		bundle.putString(WeatherDetailFragment.LAT, weather.getCoords().lat)
		bundle.putString(WeatherDetailFragment.LON, weather.getCoords().lon)
		bundle.putString(WeatherDetailFragment.TITLE, title)
		bundle.putString(WeatherDetailFragment.SELECTED_DATE, weather.dayNumber)
		fragment.arguments = bundle
		transaction?.addToBackStack(null)
		transaction?.replace(R.id.fragment_container, fragment)
		transaction?.commit()
	}

	private fun onRefreshData(fragmentContainer: SwipeRefreshLayout) {
		viewModel.loadData(this.city, "", "", false, "")
		fragmentContainer.isRefreshing = false
	}

	companion object{
		val animWeather = listOf(
			"https://openweathermap.org/img/wn/02d@4x.png",
			"https://openweathermap.org/img/wn/10d@4x.png",
			"https://openweathermap.org/img/wn/02d@4x.png",
			"https://openweathermap.org/img/wn/02n@4x.png",
		)
	}
}