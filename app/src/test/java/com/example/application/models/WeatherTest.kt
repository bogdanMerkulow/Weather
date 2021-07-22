package com.example.application.models

import com.example.application.api.*
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class WeatherTest {

    private lateinit var weather: Weather

    @Before
    fun setup() {
        weather = Weather(iconName = "10n", temp = 29.0f)
    }

    @Test
    fun getIconUrl() {
        assertEquals(weather.getIconUrl(), "https://openweathermap.org/img/wn/10n@4x.png")
    }

    @Test
    fun getTemp() {
        assertEquals(weather.getTemp(), "29°C")
        assertEquals(Weather(temp = 31.0f).getTemp(), "31\uD83D\uDD25")
    }

    @Test
    fun responseConvert() {
        val weatherResponse = WeatherResponse(
            list = arrayListOf(
                WeatherList(
                    weather = arrayListOf(
                        WeatherToday(
                            description = "ясно",
                            icon = "10n"
                        )
                    ),
                    main = WeatherMain(
                        temp = 32.0f + 272.15f
                    )
                )
            ),
            city = City(
                name = "Тамбов",
                coord = WeatherCoord(
                    lat = 10.0f,
                    lon = 10.0f
                )
            )
        )
        assertEquals(
            Weather.responseConvert(
                weatherResponse.list[0],
                weatherResponse,
                "Wed 13.07 03:00",
                "10"
            ),
            Weather(
                iconName = "10n",
                title = "Wed 13.07 03:00",
                temp = 32.0f,
                state = "ясно",
                city = "Тамбов",
                lat = "10.0",
                lon = "10.0",
                dayNumber = "10"
            )
        )
    }
}