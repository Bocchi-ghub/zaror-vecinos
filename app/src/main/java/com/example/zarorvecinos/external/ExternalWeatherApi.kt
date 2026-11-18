package com.example.zarorvecinos.external

import retrofit2.http.GET
import retrofit2.http.Query

interface ExternalWeatherApi {
    @GET("forecast")
    suspend fun getSantiagoWeather(
        @Query("latitude") latitude: Double = -33.4489,
        @Query("longitude") longitude: Double = -70.6693,
        @Query("current") current: String = "temperature_2m,wind_speed_10m"
    ): WeatherResponse
}