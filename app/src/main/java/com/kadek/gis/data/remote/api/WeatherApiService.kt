package com.kadek.gis.data.remote.api

import com.kadek.gis.data.remote.response.WeatherResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    @GET("2.5/onecall?")
    fun getWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("exclude") exclude: String = "minutely,hourly,alerts",
        @Query("units") units: String = "metric",
        @Query("appid") appId: String = "8c1c230db9d1ef99bf33660785575a51",
    ) : Call<WeatherResponse>
}