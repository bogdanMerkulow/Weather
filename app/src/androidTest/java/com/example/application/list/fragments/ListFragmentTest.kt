package com.example.application.list.fragments

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.example.application.MainActivity
import com.example.application.R
import org.junit.Before
import org.junit.Test

class ListFragmentTest {

    @Before
    fun setup() {
        ActivityScenario.launch(MainActivity::class.java)
        Thread.sleep(2000)
    }

    @Test
    fun changeLocation() {
        Espresso.onView(withId(R.id.change_city)).perform(click())
        Espresso.onView(withId(R.id.city_edit_text)).check(matches(isDisplayed()))
        Espresso.onView(withId(R.id.city_edit_text)).perform(typeText("tambov"))
        Espresso.onView(withText("enter")).perform(click())
        Espresso.onView(withText("Тамбов"))
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        Espresso.onView(withId(R.id.progress_circular))
            .check(matches(withEffectiveVisibility(Visibility.INVISIBLE)))
    }

    @Test
    fun scrollAndClickOnItem() {
        Espresso.onView(withId(R.id.rv_weather_list)).perform(scrollTo(), click())
        Thread.sleep(2000)
        Espresso.onView(withId(R.id.progress_circular))
            .check(matches(withEffectiveVisibility(Visibility.INVISIBLE)))
    }

    @Test
    fun backButton() {
        Espresso.onView(withId(R.id.rv_weather_list)).perform(scrollTo(), click())
        pressBack()
        Thread.sleep(1000)
        Espresso.onView(withId(R.id.progress_circular))
            .check(matches(withEffectiveVisibility(Visibility.INVISIBLE)))
    }
}