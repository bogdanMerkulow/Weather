package com.example.application.detail.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.application.R
import com.example.application.adapters.RecyclerViewAdapter
import com.example.application.databinding.FragmentWeatherDetailBinding
import com.example.application.detail.viewmodels.WeatherDetailViewModel
import com.example.application.factories.ViewModelFactory
import com.example.application.factories.WeatherViewHolderFactory
import com.example.application.models.Weather

class WeatherDetailFragment : Fragment() {
    private lateinit var adapter: RecyclerViewAdapter<Weather>
    private lateinit var viewModel: WeatherDetailViewModel
    private lateinit var viewModelFactory: ViewModelFactory
    private lateinit var actionBar: ActionBar
    private lateinit var binding: FragmentWeatherDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentWeatherDetailBinding.inflate(layoutInflater)
        viewModelFactory = ViewModelFactory(activity)
        viewModel =
            ViewModelProvider(this, viewModelFactory).get(WeatherDetailViewModel::class.java)
        adapter = object : RecyclerViewAdapter<Weather>(WeatherViewHolderFactory()) {
            override fun getLayoutId(viewType: Int): Int = R.layout.weather_list
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rootView = binding.root
        val rcWeatherList: RecyclerView = binding.rvWeatherList
        val progress: RelativeLayout = binding.progressCircular

        rcWeatherList.adapter = adapter

        viewModel.loadData(
            arguments?.getString(CITY)!!,
            arguments?.getString(SELECTED_DATE)!!
        )

        viewModel.data.observe(viewLifecycleOwner) { data ->
            adapter.addItems(data)
        }

        viewModel.reload.observe(viewLifecycleOwner) { reload ->
            onLiveDataChangeReload(reload, progress)
        }

        viewModel.title.observe(viewLifecycleOwner) { title ->
            activity?.title = title
        }

        return rootView
    }

    private fun onLiveDataChangeReload(reload: Boolean, progress: RelativeLayout) {
        if (reload) {
            progress.visibility = View.VISIBLE
        } else {
            progress.visibility = View.INVISIBLE
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        actionBar = (activity as AppCompatActivity).supportActionBar!!
        actionBar.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeButtonEnabled(true)
        }
    }

    override fun onDetach() {
        super.onDetach()
        actionBar.apply {
            setDisplayHomeAsUpEnabled(false)
            setHomeButtonEnabled(false)
        }
    }

    companion object {
        const val CITY = "q"
        const val SELECTED_DATE = "date"
    }
}