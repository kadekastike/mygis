package com.kadek.gis.viewmodel

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kadek.gis.data.remote.api.ApiConfig
import com.kadek.gis.data.remote.response.Current
import com.kadek.gis.data.remote.response.WeatherItem
import com.kadek.gis.data.remote.response.WeatherResponse
import com.kadek.gis.ui.MainActivity
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback

@SuppressLint("StaticFieldLeak")
class WeatherViewModel(): ViewModel() {
    private val _humidity = MutableLiveData<Current>()
    val humidity: LiveData<Current> = _humidity

    private val _currentWeather = MutableLiveData<List<WeatherItem?>>()
    val currentWeather: LiveData<List<WeatherItem?>> = _currentWeather

    fun getWeather(latitude:Double, longitude: Double) {
        val client = ApiConfig.getWeatherApiService().getWeather(latitude, longitude)
        client.enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(
                call: Call<WeatherResponse>,
                response: Response<WeatherResponse>
            ) {
                if (response.isSuccessful) {
                    _humidity.value = response.body()?.current
                    _currentWeather.value = response.body()?.current?.weather
                }
                else {
                    Log.d("Error", response.message())
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                Log.d("Errorr data", t.message.toString())
            }

        })
    }
}