package com.kadek.gis.data.remote.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfig {
    companion object{
        fun getApiService(): ApiService {
            val retrofit = Retrofit.Builder()
                .baseUrl("http://aaf7-116-206-39-95.ngrok.io/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(ApiService::class.java)
        }
    }
}