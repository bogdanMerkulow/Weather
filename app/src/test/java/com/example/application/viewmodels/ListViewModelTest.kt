package com.example.application.viewmodels

import com.example.application.list.viewmodels.WeatherListViewModel
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.spy
import com.nhaarman.mockitokotlin2.verify
import org.junit.Before
import org.junit.Test

class ListViewModelTest {

    private lateinit var viewModel: WeatherListViewModel

    @Before
    fun setup() {
        viewModel = spy(WeatherListViewModel(mock(), mock(), mock()))
    }

    @Test
    fun changeLocation() {
        viewModel.changeLocation("test")
        verify(viewModel).changeLocation("test")
        verify(viewModel).loadData()
    }
}