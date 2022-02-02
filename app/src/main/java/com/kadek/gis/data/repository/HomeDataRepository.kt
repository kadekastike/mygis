package com.kadek.gis.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kadek.gis.data.model.Area
import com.kadek.gis.data.model.Master
import com.kadek.gis.data.model.Section
import com.kadek.gis.data.model.Weather
import com.kadek.gis.data.remote.response.*
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

    override fun getMaster(): LiveData<List<Master>> {
        val masterResult = MutableLiveData<List<Master>>()
        CoroutineScope(IO).launch {
            remoteDataSource.getMaster(object : RemoteDataSource.loadMasterCallback {
                override fun onAllMasterReceived(masterResponse: List<MasterResponseItem>) {
                    val masterList = ArrayList<Master>()
                    for (response in masterResponse) {
                        val master = Master(
                            response.title,
                            response.total,
                            response.url
                        )
                        masterList.add(master)
                    }
                    masterResult.postValue(masterList)
                }
            })
        }
        return  masterResult
    }

    override fun getPlantationGroup(): LiveData<List<Area>> {
        val pgResult = MutableLiveData<List<Area>>()
        CoroutineScope(IO).launch {
            remoteDataSource.getPlantationGroup(object : RemoteDataSource.loadAreaCallback {
                override fun onAllAreaReceived(areaResponse: List<ListAreaResponse>) {
                    val areaList = ArrayList<Area>()
                    for (response in areaResponse) {
                        val area = Area(response.id,
                            response.name,
                            response.pg,
                            response.area,
                            response.location,
                            response.section,
                            response.chief,
                            response.url,
                            response.created_at,
                            response.updated_at
                        )
                        areaList.add(area)
                    }
                    pgResult.postValue(areaList)
                }
            })
        }
        return pgResult
    }

    override fun getAreas(pgId: Int): LiveData<List<Area>> {
        val areaResult = MutableLiveData<List<Area>>()
        CoroutineScope(IO).launch {
            remoteDataSource.getAreas(pgId, object : RemoteDataSource.loadAreaCallback {
                override fun onAllAreaReceived(areaResponse: List<ListAreaResponse>) {
                    val areaList = ArrayList<Area>()
                    for (response in areaResponse) {
                        val area = Area(response.id,
                            response.name,
                            response.pg,
                            response.area,
                            response.location,
                            response.section,
                            response.chief,
                            response.url,
                            response.created_at,
                            response.updated_at
                        )
                        areaList.add(area)
                    }
                    areaResult.postValue(areaList)
                }
            })
        }
        return areaResult
    }

    override fun getLocations(pgId: Int, areaId: Int): LiveData<List<Area>> {
        val locationsResult = MutableLiveData<List<Area>>()
        CoroutineScope(IO).launch {
            remoteDataSource.getLocations(pgId, areaId, object : RemoteDataSource.loadAreaCallback {
                override fun onAllAreaReceived(areaResponse: List<ListAreaResponse>) {
                    val areaList = ArrayList<Area>()
                    for (response in areaResponse) {
                        val area = Area(response.id,
                            response.name,
                            response.pg,
                            response.area,
                            response.location,
                            response.section,
                            response.chief,
                            response.url,
                            response.created_at,
                            response.updated_at
                        )
                        areaList.add(area)
                    }
                    locationsResult.postValue(areaList)
                }
            })
        }
        return locationsResult
    }

    override fun getSections(pgId: Int, areaId: Int, locationId: Int): LiveData<List<Area>> {
        val sectionsResult = MutableLiveData<List<Area>>()
        CoroutineScope(IO).launch {
            remoteDataSource.getSections(pgId, areaId, locationId, object : RemoteDataSource.loadAreaCallback {
                override fun onAllAreaReceived(areaResponse: List<ListAreaResponse>) {
                    val areaList = ArrayList<Area>()
                    for (response in areaResponse) {
                        val area = Area(response.id,
                            response.name,
                            response.pg,
                            response.area,
                            response.location,
                            response.section,
                            response.chief,
                            response.url,
                            response.created_at,
                            response.updated_at
                        )
                        areaList.add(area)
                    }
                    sectionsResult.postValue(areaList)
                }
            })
        }
        return sectionsResult
    }

    override fun getDetailSection(sectionId: Long): LiveData<Section> {
        val sectionResult = MutableLiveData<Section>()
        CoroutineScope(IO).launch {
            remoteDataSource.getDetailSection(sectionId, object : RemoteDataSource.loadMapDetailCallback {
                override fun onMapDetailReceived(sectionResponse: SectionResponse) {
                    val mapDetail = Section(sectionResponse.data?.id,
                        sectionResponse.data?.name,
                        sectionResponse.data?.geografi!!.swLatitude.toDouble(),
                        sectionResponse.data.geografi.swLongitude.toDouble(),
                        sectionResponse.data.geografi.neLatitude.toDouble(),
                        sectionResponse.data.geografi.neLongitude.toDouble(),
                        sectionResponse.data.geografi.gambarTaksasi,
                        sectionResponse.data.geografi.gambarNdvi,
                        sectionResponse.data.geografi.age,
                        sectionResponse.data.geografi.variaty,
                        sectionResponse.data.geografi.crop,
                        sectionResponse.data.geografi.forcingTime,
                        sectionResponse.data.geografi.createdAt,
                        sectionResponse.data.geografi.updatedAt
                    )
                    sectionResult.postValue(mapDetail)
                }
            })
        }
        return sectionResult
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