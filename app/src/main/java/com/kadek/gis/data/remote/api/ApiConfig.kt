package com.kadek.gis.data.remote.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfig {
    companion object{
        private const val baseUrl = "http://30ca-110-137-36-18.ngrok.io"
        fun getApiService(): MapApiService {
            val retrofit = Retrofit.Builder()
                .baseUrl(
                    "$baseUrl/api/"
                )
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(MapApiService::class.java)
        }
        fun getWeatherApiService() : WeatherApiService {
            val loggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()
            val retrofit = Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/data/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            return retrofit.create(WeatherApiService::class.java)
        }
    }
}