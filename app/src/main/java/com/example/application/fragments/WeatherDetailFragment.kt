package com.example.application.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.application.R
import com.example.application.models.Weather
import com.example.application.adapters.WeatherDetailRecyclerViewAdapter
import com.example.application.ViewModels.WeatherViewModel

class WeatherDetailFragment : Fragment() {
	private lateinit var adapter: WeatherDetailRecyclerViewAdapter
	private lateinit var viewModel: WeatherViewModel
	private lateinit var actionBar: ActionBar

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		viewModel = ViewModelProvider(this)[WeatherViewModel::class.java]
		adapter = WeatherDetailRecyclerViewAdapter()
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
		val rootView = inflater.inflate(R.layout.fragment_weather_detail, container, false)
		val rcWeatherList: RecyclerView = rootView.findViewById(R.id.rv_weather_list)
		val progress: ProgressBar = rootView.findViewById(R.id.progress_circular)

		rcWeatherList.layoutManager = LinearLayoutManager(activity)
		rcWeatherList.adapter = adapter

		activity?.title = arguments?.getString(TITLE)

		viewModel.loadData(
			arguments?.getString(CITY)!!,
			arguments?.getString(LAT)!!,
			arguments?.getString(LON)!!,
			true,
			arguments?.getString(SELECTED_DATE)!!
		)

		viewModel.getData().observe(viewLifecycleOwner) { data ->
			adapter.addWeather(data as MutableList<Weather>)
		}

		viewModel.isReload().observe(viewLifecycleOwner) { reload ->
			onLiveDataChangeReload(reload, progress)
		}

		return rootView
	}

	private fun onLiveDataChangeReload(reload: Boolean, progress: ProgressBar) {
		if(reload){
			progress.visibility = View.VISIBLE
		}else{
			progress.visibility = View.INVISIBLE
		}
	}

	override fun onAttach(context: Context) {
		super.onAttach(context)
		actionBar = (activity as AppCompatActivity).supportActionBar!!
		actionBar.setDisplayHomeAsUpEnabled(true)
		actionBar.setHomeButtonEnabled(true)
	}

	override fun onDetach() {
		super.onDetach()
		actionBar.setDisplayHomeAsUpEnabled(false)
		actionBar.setHomeButtonEnabled(false)
	}

	companion object{
		const val CITY = "q"
		const val LON = "lon"
		const val LAT = "lat"
		const val TITLE = "title"
		const val SELECTED_DATE = "date"
	}
}