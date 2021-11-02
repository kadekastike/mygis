package com.kadek.gis.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kadek.gis.MapResponse
import com.kadek.gis.data.model.Maps
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class HomeDataRepository private constructor(private val remoteDataSource: RemoteDataSource) : HomeDataSource{
    companion object {
        @Volatile
        private var instance: HomeDataRepository? = null

        fun getInstance(remoteDataSource: RemoteDataSource): HomeDataRepository= instance ?: synchronized(this) {
            instance ?: HomeDataRepository(remoteDataSource)
        }
    }

    override fun getMaps(): LiveData<List<Maps>> {
        val mapResult = MutableLiveData<List<Maps>>()
        CoroutineScope(IO).launch {
            remoteDataSource.getMaps(object: RemoteDataSource.loadMapsCallback{
                override fun onAllMapsReceived(mapResponse: List<MapResponse>) {
                    val mapList = ArrayList<Maps>()
                    for (response in mapResponse) {
                        val map = Maps(response.id,
                            response.name,
                            response.swLatitude,
                            response.swLongitude,
                            response.neLatitude,
                            response.neLongitude,
                            response.gambarTaksasi,
                            response.gambarNdvi,
                            response.createdAt,
                            response.updatedAt
                        )
                        mapList.add(map)
                    }
                    mapResult.postValue(mapList)
                }
            })
        }
        return mapResult
    }

    override fun getDetailMap(id: Int): LiveData<Maps> {
        val mapResult = MutableLiveData<Maps>()
        CoroutineScope(IO).launch {
            remoteDataSource.getMapDetail(id, object : RemoteDataSource.loadMapDetailCallback {
                override fun onMapDetailReceived(mapResponse: MapResponse) {
                    val mapDetail = Maps(mapResponse.id,
                        mapResponse.name,
                        mapResponse.swLatitude,
                        mapResponse.swLongitude,
                        mapResponse.neLatitude,
                        mapResponse.neLongitude,
                        mapResponse.gambarTaksasi,
                        mapResponse.gambarNdvi,
                        mapResponse.createdAt,
                        mapResponse.updatedAt
                    )
                    mapResult.postValue(mapDetail)
                }
            })
        }
        return mapResult
    }
}