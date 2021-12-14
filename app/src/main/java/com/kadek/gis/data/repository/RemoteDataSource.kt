package com.kadek.gis.data.repository

import com.kadek.gis.data.remote.response.MapResponse
import com.kadek.gis.data.remote.api.ApiConfig
import com.kadek.gis.data.remote.response.AreaResponse
import com.kadek.gis.data.remote.response.DailyItem
import com.kadek.gis.utils.IdlingResource.idlingResource
import retrofit2.await

class RemoteDataSource {
    companion object {
        @Volatile
        private var instance: RemoteDataSource? = null
        fun getInstance(): RemoteDataSource = instance ?: synchronized(this) {
            instance ?: RemoteDataSource()
        }
    }

    suspend fun getAreas(callback: loadAreaCallback) {
        idlingResource.increment()
        ApiConfig.getApiService().getAreas().await().data?.let { areas ->
            callback.onAllAreaReceived(areas)
            idlingResource.decrement()
        }
    }

    suspend fun getMaps(areaId : Int, callback: loadMapsCallback) {
        idlingResource.increment()
        ApiConfig.getApiService().getMaps(areaId).await().data?.let { maps ->
            callback.onAllMapsReceived(maps)
            idlingResource.decrement()
        }
    }

    suspend fun getMapDetail(id: Int, callback: loadMapDetailCallback){
        idlingResource.increment()
        ApiConfig.getApiService().getMapDetail(id).await().let { maps ->
            callback.onMapDetailReceived(maps)
            idlingResource.decrement()
        }
    }

    suspend fun getWeather(lat: Double, long: Double, callback: loadWeatherCallback) {
        idlingResource.increment()
        ApiConfig.getWeatherApiService().getWeather(lat, long).await().daily?.let { weather ->
            callback.onAllWeatherReceived(weather)
            idlingResource.decrement()
        }

    }

    interface loadWeatherCallback {
        fun onAllWeatherReceived(weatherResponse: List<DailyItem?>)
    }

    interface loadAreaCallback {
        fun onAllAreaReceived(areaResponse: List<AreaResponse>)
    }

    interface loadMapsCallback {
        fun onAllMapsReceived(mapResponse: List<MapResponse>)
    }

    interface  loadMapDetailCallback {
        fun onMapDetailReceived(mapResponse: MapResponse)
    }
}