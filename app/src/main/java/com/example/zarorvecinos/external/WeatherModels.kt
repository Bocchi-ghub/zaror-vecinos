package com.example.zarorvecinos.external

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @SerializedName("current")
    val currentWeather: CurrentWeather
)

data class CurrentWeather(
    @SerializedName("temperature_2m")
    val temperature: Double,
    @SerializedName("wind_speed_10m")
    val windspeed: Double,
    val weathercode: Int = 0
)