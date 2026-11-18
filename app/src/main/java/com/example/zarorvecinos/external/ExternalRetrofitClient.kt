package com.example.zarorvecinos.external

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ExternalRetrofitClient {
    private const val BASE_URL = "https://api.open-meteo.com/v1/"

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .header("User-Agent", "ZarorVecinosApp/1.0")
                .build()
            chain.proceed(request)
        }
        .build()

    val api: ExternalWeatherApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ExternalWeatherApi::class.java)
    }
}