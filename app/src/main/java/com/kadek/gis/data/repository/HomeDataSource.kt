package com.kadek.gis.data.repository

import androidx.lifecycle.LiveData
import com.kadek.gis.data.model.Area
import com.kadek.gis.data.model.Maps
import com.kadek.gis.data.model.Weather
import com.kadek.gis.data.remote.response.DailyItem

interface HomeDataSource {

    fun getAreas(): LiveData<List<Area>>

    fun getMaps(areaId: Int): LiveData<List<Maps>>

    fun getDetailMap(id: Int): LiveData<Maps>

    fun getWeather(lat: Double, long: Double): LiveData<List<Weather?>>
}