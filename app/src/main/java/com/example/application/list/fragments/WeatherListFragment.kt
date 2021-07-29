package com.example.application.list.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.example.application.R
import com.example.application.adapters.RecyclerViewAdapter
import com.example.application.databinding.FragmentWeatherListBinding
import com.example.application.detail.fragments.WeatherDetailFragment
import com.example.application.factories.ViewModelFactory
import com.example.application.factories.WeatherViewHolderFactory
import com.example.application.list.viewmodels.WeatherListViewModel
import com.example.application.models.SelectedWeather
import com.example.application.models.Weather
import timber.log.Timber

class WeatherListFragment : Fragment() {
    private lateinit var adapter: RecyclerViewAdapter<Weather>
    private lateinit var listViewModel: WeatherListViewModel
    private lateinit var viewModelFactory: ViewModelFactory
    private var _binding: FragmentWeatherListBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = FragmentWeatherListBinding.inflate(layoutInflater)
        viewModelFactory = ViewModelFactory()
        listViewModel =
            ViewModelProvider(this, viewModelFactory).get(WeatherListViewModel::class.java)


        listViewModel.loadLocation()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rootView = binding.root
        val rcWeatherList: RecyclerView = binding.rvWeatherList
        val progress: RelativeLayout = binding.progressCircular
        val headerText: TextView = binding.headerText
        val headerImage: ImageView = binding.headerImage
        val changeCityButton = binding.changeCity
        val fragmentContainer = binding.swipeRefresh

        adapter = RecyclerViewAdapter(
            WeatherViewHolderFactory(),
            R.layout.weather_list,
            listViewModel::itemClick
        )

        rcWeatherList.adapter = adapter

        fragmentContainer.setOnRefreshListener {
            fragmentContainer.isRefreshing = listViewModel.loadData().isCompleted
        }

        changeCityButton.setOnClickListener {
            onClickChangeCityButton()
        }

        listViewModel.title.observe(viewLifecycleOwner) { title ->
            activity?.title = title
        }

        listViewModel.header.observe(viewLifecycleOwner) { header ->
            headerText.text = header
        }

        listViewModel.headerImageUrl.observe(viewLifecycleOwner) { imageUrl ->
            setHeaderImageUrl(imageUrl, headerImage)
        }

        listViewModel.reload.observe(viewLifecycleOwner) { reload ->
            progress.visibility = reload
        }

        listViewModel.data.observe(viewLifecycleOwner) { data ->
            adapter.addItems(data)
        }

        listViewModel.clickData.observe(viewLifecycleOwner) { data ->
            onItemClick(data)
        }

        return rootView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onClickChangeCityButton() {
        val dialogFragment = CityDialogFragment(listViewModel)
        val manager = parentFragmentManager
        dialogFragment.show(manager, "changeCity")
    }

    private fun onItemClick(data: SelectedWeather) {
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        val fragment = WeatherDetailFragment()
        val bundle = Bundle()
        bundle.putString(WeatherDetailFragment.CITY, data.city)
        bundle.putString(WeatherDetailFragment.SELECTED_DATE, data.day)
        Timber.i("Click on item where day: ${data.day}")
        fragment.arguments = bundle
        transaction?.apply {
            addToBackStack(null)
            add(R.id.fragment_container, fragment)
            commit()
        }
    }

    private fun setHeaderImageUrl(url: String, headerImage: ImageView) {
        Glide
            .with(this)
            .load(url)
            .into(headerImage)
    }
}