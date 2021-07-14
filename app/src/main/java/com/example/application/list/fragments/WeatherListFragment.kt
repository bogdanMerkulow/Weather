package com.example.application.list.fragments

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
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.example.application.R
import com.example.application.adapters.RecyclerViewAdapter
import com.example.application.detail.fragments.WeatherDetailFragment
import com.example.application.factories.ViewModelFactory
import com.example.application.factories.WeatherViewHolderFactory
import com.example.application.list.viewmodels.WeatherListViewModel
import com.example.application.models.Weather

class WeatherListFragment : Fragment() {
    private lateinit var adapter: RecyclerViewAdapter<Weather>
    private lateinit var listViewModel: WeatherListViewModel
    private lateinit var viewModelFactory: ViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModelFactory = ViewModelFactory()
        listViewModel =
            ViewModelProvider(this, viewModelFactory).get(WeatherListViewModel::class.java)
        adapter =
            object : RecyclerViewAdapter<Weather>(WeatherViewHolderFactory(), this::onItemClick) {
                override fun getLayoutId(viewType: Int): Int = R.layout.weather_list
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_weather_list, container, false)
        val rcWeatherList: RecyclerView = rootView.findViewById(R.id.rv_weather_list)
        val progress: ProgressBar = rootView.findViewById(R.id.progress_circular)
        val headerText: TextView = rootView.findViewById(R.id.header_text)
        val imageAnimation = rootView.findViewById<ImageView>(R.id.header_image_animation)
        val headerImage: ImageView = rootView.findViewById(R.id.header_image)
        val changeCityButton = rootView.findViewById<Button>(R.id.change_city)
        val fragmentContainer = rootView.findViewById<SwipeRefreshLayout>(R.id.swipe_refresh)

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
            setHeaderImageUrl(imageUrl, headerImage, imageAnimation)
        }

        listViewModel.reload.observe(viewLifecycleOwner) { reload ->
            onLiveDataChangeReload(reload, progress)
        }

        listViewModel.data.observe(viewLifecycleOwner) { data ->
            adapter.addItems(data)
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

    private fun onLiveDataChangeReload(reload: Boolean, progress: ProgressBar) {
        if (reload) {
            progress.visibility = View.VISIBLE
        } else {
            progress.visibility = View.INVISIBLE
        }
    }

    fun onItemClick(data: Weather) {
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        val fragment = WeatherDetailFragment()
        val bundle = Bundle()
        bundle.putString(WeatherDetailFragment.CITY, data.city)
        bundle.putString(WeatherDetailFragment.SELECTED_DATE, data.dayNumber)
        fragment.arguments = bundle
        transaction?.apply {
            addToBackStack(null)
            replace(R.id.fragment_container, fragment)
            commit()
        }
    }

    private fun onRefreshData(fragmentContainer: SwipeRefreshLayout) {
        listViewModel.loadData()
        fragmentContainer.isRefreshing = false
    }

    private fun setHeaderImageUrl(url: String, headerImage: ImageView, imageAnimation: ImageView) {
        imageAnimation.visibility = View.GONE
        imageAnimation.clearAnimation()
        if (url in animWeatherUrls) {
            val animationRotateCenter: Animation = AnimationUtils.loadAnimation(
                activity, R.anim.gray_spinner_png
            )
            imageAnimation.visibility = View.VISIBLE
            imageAnimation.startAnimation(animationRotateCenter)
        }

        Glide
            .with(this)
            .load(url)
            .into(headerImage)
    }

    companion object {
        val animWeatherUrls = listOf(
            "https://openweathermap.org/img/wn/02d@4x.png",
            "https://openweathermap.org/img/wn/10d@4x.png",
            "https://openweathermap.org/img/wn/02d@4x.png",
            "https://openweathermap.org/img/wn/02n@4x.png",
        )
    }
}