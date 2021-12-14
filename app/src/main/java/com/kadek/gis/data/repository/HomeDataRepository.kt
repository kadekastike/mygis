package com.kadek.gis.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kadek.gis.data.model.Area
import com.kadek.gis.data.remote.response.MapResponse
import com.kadek.gis.data.model.Maps
import com.kadek.gis.data.model.Weather
import com.kadek.gis.data.remote.response.AreaResponse
import com.kadek.gis.data.remote.response.DailyItem
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

    override fun getAreas(): LiveData<List<Area>> {
        val areaResult = MutableLiveData<List<Area>>()
        CoroutineScope(IO).launch {
            remoteDataSource.getAreas(object : RemoteDataSource.loadAreaCallback {
                override fun onAllAreaReceived(areaResponse: List<AreaResponse>) {
                    val areaList = ArrayList<Area>()
                    for (response in areaResponse) {
                        val area = Area(response.id,
                            response.name,
                            response.createdAt,
                            response.updatedAt
                        )
                        areaList.add(area)
                    }
                    areaResult.postValue(areaList)
                }
            })
        }
        return areaResult
    }
    override fun getMaps(areaId : Int): LiveData<List<Maps>> {
        val mapResult = MutableLiveData<List<Maps>>()
        CoroutineScope(IO).launch {
            remoteDataSource.getMaps(areaId, object: RemoteDataSource.loadMapsCallback{
                override fun onAllMapsReceived(mapResponse: List<MapResponse>) {
                    val mapList = ArrayList<Maps>()
                    for (response in mapResponse) {
                        val map = Maps(response.id,
                            response.name,
                            response.area,
                            response.createdBy,
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
                        mapResponse.area,
                        mapResponse.createdBy,
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

    override fun getWeather(lat: Double, long: Double): LiveData<List<Weather?>> {
        val weatherResult = MutableLiveData<List<Weather?>>()
        CoroutineScope(IO).launch {
            remoteDataSource.getWeather(lat, long, object : RemoteDataSource.loadWeatherCallback {
                override fun onAllWeatherReceived(weatherResponse: List<DailyItem?>) {
                    val weatherList = ArrayList<Weather?>()
                    for (response in weatherResponse) {
                        response?.weather?.map {
                            val weatherDetail = response.humidity?.let { humidity ->
                                response.dt?.let { dt ->
                                    it?.description?.let { description ->
                                        it.icon?.let { icon ->
                                            Weather(
                                                dt,
                                                humidity,
                                                description,
                                                icon
                                            )
                                        }
                                    }
                                }

                            }
                            weatherList.add(weatherDetail)
                        }
                        weatherResult.postValue(weatherList)
                    }
                }
            })
        }
        return weatherResult
    }
}