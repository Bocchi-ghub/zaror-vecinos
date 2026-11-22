package com.example.zarorvecinos.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

data class IndicadoresResponse(
    val uf: IndicadorDetalle,
    val dolar: IndicadorDetalle
)

data class IndicadorDetalle(
    val nombre: String,
    val valor: Double
)

interface IndicadorApiService {
    @GET("api")
    suspend fun obtenerIndicadores(): IndicadoresResponse
}

object ExternalRetrofitClient {
    private const val BASE_URL = "https://mindicador.cl/"

    val api: IndicadorApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(IndicadorApiService::class.java)
    }
}