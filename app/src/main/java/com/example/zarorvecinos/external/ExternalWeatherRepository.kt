package com.example.zarorvecinos.external

class ExternalWeatherRepository(
    private val api: ExternalWeatherApi = ExternalRetrofitClient.api
) {
    suspend fun fetchCurrentWeather(): Result<CurrentWeather> {
        return try {
            val response = api.getSantiagoWeather()
            Result.success(response.currentWeather)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}