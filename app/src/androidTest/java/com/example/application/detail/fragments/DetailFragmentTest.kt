package com.example.application.detail.fragments

import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import com.example.application.MainActivity
import com.example.application.R
import org.junit.Before
import org.junit.Test

class DetailFragmentTest {

    @Before
    fun setup() {
        ActivityScenario.launch(MainActivity::class.java)
        Thread.sleep(DELAY)
        onView(withId(R.id.rv_weather_list))
            .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))
        Thread.sleep(DELAY)
    }

    @Test
    fun loader() {
        onView(withId(R.id.progress_circular))
            .check(matches(withEffectiveVisibility(Visibility.INVISIBLE)))
    }

    @Test
    fun backButton() {
        Espresso.pressBack()
        onView(withId(R.id.progress_circular))
            .check(matches(withEffectiveVisibility(Visibility.INVISIBLE)))
        onView(withId(R.id.header))
            .check(matches(isDisplayed()))
    }

    companion object {
        private const val DELAY: Long = 2000
    }
}