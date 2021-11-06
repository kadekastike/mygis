package com.kadek.gis.data.repository

import com.kadek.gis.data.remote.response.MapResponse
import com.kadek.gis.data.remote.api.ApiConfig
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

    suspend fun getMaps(callback: loadMapsCallback) {
        idlingResource.increment()
        ApiConfig.getApiService().getMaps().await().data?.let { maps ->
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

    interface loadMapsCallback {
        fun onAllMapsReceived(mapResponse: List<MapResponse>)
    }

    interface  loadMapDetailCallback {
        fun onMapDetailReceived(mapResponse: MapResponse)
    }
}