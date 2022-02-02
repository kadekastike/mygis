package com.kadek.gis.data.repository

import com.kadek.gis.data.remote.api.ApiConfig
import com.kadek.gis.data.remote.response.*
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

    suspend fun getMaster(callback: loadMasterCallback) {
        idlingResource.increment()
        ApiConfig.getApiService().getMaster().await().let { masters ->
            callback.onAllMasterReceived(masters)
            idlingResource.decrement()
        }
    }

    suspend fun getPlantationGroup(callback: loadAreaCallback) {
        idlingResource.increment()
        ApiConfig.getApiService().getPlantationGroup().await().data?.let { pgs ->
            callback.onAllAreaReceived(pgs)
            idlingResource.decrement()
        }
    }

    suspend fun getAreas(pgId: Int, callback: loadAreaCallback) {
        idlingResource.increment()
        ApiConfig.getApiService().getAreas(pgId).await().data?.let { areas ->
            callback.onAllAreaReceived(areas)
            idlingResource.decrement()
        }
    }

    suspend fun getLocations(pgId: Int, areaId: Int, callback: loadAreaCallback) {
        idlingResource.increment()
        ApiConfig.getApiService().getLocations(pgId, areaId).await().data?.let { locations ->
            callback.onAllAreaReceived(locations)
            idlingResource.decrement()
        }
    }

    suspend fun getSections(pgId: Int, areaId: Int, locationId: Int, callback: loadAreaCallback) {
        idlingResource.increment()
        ApiConfig.getApiService().getSections(pgId, areaId, locationId).await().data?.let { sections ->
            callback.onAllAreaReceived(sections)
            idlingResource.decrement()
        }
    }

    suspend fun getDetailSection(sectionId: Long, callback: loadMapDetailCallback){
        idlingResource.increment()
        ApiConfig.getApiService().getDetailSection(sectionId).await().let { section ->
            callback.onMapDetailReceived(section)
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
        fun onAllAreaReceived(areaResponse: List<ListAreaResponse>)
    }

    interface loadMapDetailCallback {
        fun onMapDetailReceived(mapResponse: SectionResponse)
    }

    interface loadMasterCallback {
        fun onAllMasterReceived(masterResponse: List<MasterResponseItem>)
    }
}