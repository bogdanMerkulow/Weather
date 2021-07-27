package com.example.application.list.fragments

import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import com.example.application.MainActivity
import com.example.application.R
import org.junit.Before
import org.junit.Test

class ListFragmentTest {

    @Before
    fun setup() {
        ActivityScenario.launch(MainActivity::class.java)
        Thread.sleep(DELAY)
    }

    @Test
    fun changeLocation() {
        onView(withId(R.id.change_city)).perform(click())
        onView(withId(R.id.city_edit_text)).check(matches(isDisplayed()))
        onView(withId(R.id.city_edit_text)).perform(typeText(CITY))
        onView(withText(BUTTON)).perform(click())
        onView(withText(FOUNDED_CITY))
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        onView(withId(R.id.progress_circular))
            .check(matches(withEffectiveVisibility(Visibility.INVISIBLE)))
    }

    @Test
    fun scrollAndClickOnItem() {
        onView(withId(R.id.rv_weather_list))
            .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))
        Thread.sleep(DELAY)
        onView(withId(R.id.progress_circular))
            .check(matches(withEffectiveVisibility(Visibility.INVISIBLE)))
    }

    @Test
    fun backButton() {
        onView(withId(R.id.rv_weather_list))
            .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))
        pressBack()
        Thread.sleep(DELAY)
        onView(withId(R.id.progress_circular))
            .check(matches(withEffectiveVisibility(Visibility.INVISIBLE)))
    }

    @Test
    fun pullToRefresh() {
        onView(withId(R.id.rv_weather_list))
            .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, swipeDown()))
        Thread.sleep(DELAY)
        onView(withId(R.id.progress_circular))
            .check(matches(withEffectiveVisibility(Visibility.INVISIBLE)))
    }

    companion object {
        private const val BUTTON = "enter"
        private const val FOUNDED_CITY = "Тамбов"
        private const val CITY = "tambov"
        private const val DELAY: Long = 2000
    }
}