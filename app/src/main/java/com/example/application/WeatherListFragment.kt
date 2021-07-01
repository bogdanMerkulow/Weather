package com.example.application

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide


@Suppress("LABEL_NAME_CLASH")
class WeatherListFragment : Fragment(), WeatherListRecyclerViewAdapter.Listener {
	private lateinit var adapter: WeatherListRecyclerViewAdapter
	private var q: String = ""
	private var headerImageUrl = ""
	private var title: String? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		val city = arguments?.getString("q").toString()
		if(!city.equals("null"))
			q = city
	}

	@SuppressLint("SetTextI18n")
	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		var firstElement: Boolean = true
		val rootView = inflater.inflate(R.layout.fragment_weather_list, container, false)
		val rcWeatherList: RecyclerView = rootView.findViewById(R.id.rv_weather_list)
		val progress: ProgressBar = rootView.findViewById(R.id.progress_circular)
		val headerText: TextView = rootView.findViewById(R.id.header_text)
		val headerImage: ImageView = rootView.findViewById(R.id.header_image)
		val viewModel = ViewModelProvider(this)[WeatherViewModel::class.java]
		val vmLocationData = viewModel.getLocation()
		val changeCityButton = rootView.findViewById<Button>(R.id.change_city)

		val fragmentContainer = rootView.findViewById<SwipeRefreshLayout>(R.id.swipe_refresh)
		fragmentContainer.setOnRefreshListener{
			refreshData()
			fragmentContainer.isRefreshing = false
		}

		changeCityButton.setOnClickListener{
			val builder = AlertDialog.Builder(activity)
			builder.setTitle("change city")
			val dialogLayout = inflater.inflate(R.layout.fragment_city_dialog, null)
			val editText  = dialogLayout.findViewById<EditText>(R.id.city_edit_text)
			builder.setView(dialogLayout)
			builder.setPositiveButton("enter") { _, _ ->
				val transaction = activity?.supportFragmentManager?.beginTransaction()
				val fragment = WeatherListFragment()
				val bundle = Bundle()
				bundle.putString("q", editText.text.toString())
				fragment.arguments = bundle
				transaction?.replace(R.id.fragment_container, fragment)
				transaction?.commit()
			}
			builder.show()
		}

		vmLocationData?.observe(viewLifecycleOwner, { location ->
			val vmData = viewModel.getData(q, location[0], location[1], false, "")
			vmData?.observe(viewLifecycleOwner, { data ->
				if(data[0].getError()){
					headerText.text = "city not found"
					activity?.title = ""
					progress.visibility = View.INVISIBLE
					return@observe
				}
				if(firstElement){
					headerImageUrl = data[0].getIconUrl().toString()
					Glide
						.with(this)
						.load(headerImageUrl)
						.into(headerImage)
					firstElement = false
				}
				headerText.text = data[0].getTemp()
				rcWeatherList.layoutManager = LinearLayoutManager(activity)
				adapter = WeatherListRecyclerViewAdapter(data, this)
				rcWeatherList.adapter = adapter
				progress.visibility = View.INVISIBLE
				activity?.title = data[0].getCity()
				title = data[0].getCity()
			})
		})
		return rootView
	}

	override fun onItemClick(weather: Weather) {
		val transaction = activity?.supportFragmentManager?.beginTransaction()
		val fragment = WeatherDetailFragment()
		val bundle = Bundle()
		bundle.putString("q", q)
		bundle.putString("lat", weather.getCoords()[0])
		bundle.putString("lon", weather.getCoords()[1])
		bundle.putString("title", title)
		bundle.putString("date", weather.getDayNumber())
		fragment.arguments = bundle
		transaction?.addToBackStack(null)
		transaction?.replace(R.id.fragment_container, fragment)
		transaction?.commit()
	}

	private fun refreshData() {
		val transaction = activity?.supportFragmentManager?.beginTransaction()
		val fragment = WeatherListFragment()
		transaction?.replace(R.id.fragment_container, fragment)
		transaction?.commit()
	}
}