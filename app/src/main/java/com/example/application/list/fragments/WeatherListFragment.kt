package com.example.application.list.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
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
import com.example.application.models.Weather
import timber.log.Timber

class WeatherListFragment : Fragment() {
    private lateinit var adapter: RecyclerViewAdapter<Weather>
    private lateinit var listViewModel: WeatherListViewModel
    private lateinit var viewModelFactory: ViewModelFactory
    private lateinit var binding: FragmentWeatherListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentWeatherListBinding.inflate(layoutInflater)
        viewModelFactory = ViewModelFactory(activity)
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

        adapter =
            object :
                RecyclerViewAdapter<Weather>(WeatherViewHolderFactory(), listViewModel::itemClick) {
                override fun getLayoutId(viewType: Int): Int = R.layout.weather_list
            }

        rcWeatherList.adapter = adapter

        fragmentContainer.setOnRefreshListener {
            onRefreshData(fragmentContainer)
        }

        changeCityButton.setOnClickListener {
            onClickChangeCityButton(inflater)
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
            onItemClickCallBack(data)
        }

        return rootView
    }

    private fun onClickChangeCityButton(inflater: LayoutInflater) {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle(R.string.change_city)
        val dialogLayout = inflater.inflate(R.layout.fragment_city_dialog, null)
        val editText = dialogLayout.findViewById<EditText>(R.id.city_edit_text)
        builder.setView(dialogLayout)
        builder.setPositiveButton(R.string.enter) { _, _ ->
            listViewModel.changeLocation(editText.text.toString())
        }
        builder.show()
    }

    private fun onItemClickCallBack(data: Map<String, String>) {
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        val fragment = WeatherDetailFragment()
        val bundle = Bundle()
        bundle.putString(WeatherDetailFragment.CITY, data[WeatherListViewModel.CITY])
        bundle.putString(WeatherDetailFragment.SELECTED_DATE, data[WeatherListViewModel.DAY])
        Timber.i("Click on item where day: ${data[WeatherListViewModel.DAY]}")
        fragment.arguments = bundle
        transaction?.apply {
            addToBackStack(null)
            add(R.id.fragment_container, fragment)
            commit()
        }
    }

    private fun onRefreshData(fragmentContainer: SwipeRefreshLayout) {
        listViewModel.loadData()
        fragmentContainer.isRefreshing = false
    }

    private fun setHeaderImageUrl(url: String, headerImage: ImageView) {
        Glide
            .with(this)
            .load(url)
            .into(headerImage)
    }
}