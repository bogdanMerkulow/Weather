package com.example.application

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

class WeatherDetailFragment : Fragment() {
	private lateinit var adapter: WeatherDetailRecyclerViewAdapter
	private lateinit var actionBar: ActionBar

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
		val rootView = inflater.inflate(R.layout.fragment_weather_detail, container, false)
		val rcWeatherList: RecyclerView = rootView.findViewById(R.id.rv_weather_list)
		val progress: ProgressBar = rootView.findViewById(R.id.progress_circular)
		val viewModel = ViewModelProvider(this)[WeatherViewModel::class.java]
		val vmData = viewModel.getData(arguments?.getString("q")!!, arguments?.getString("lat")!!, arguments?.getString("lon")!!, true, arguments?.getString("date")!!)

		activity?.title = arguments?.getString("title")
		vmData?.observe(viewLifecycleOwner, { data ->
			rcWeatherList.layoutManager = LinearLayoutManager(activity)
			adapter = WeatherDetailRecyclerViewAdapter(data)
			rcWeatherList.adapter = adapter
			progress.visibility = View.INVISIBLE
		})
		return rootView
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
}