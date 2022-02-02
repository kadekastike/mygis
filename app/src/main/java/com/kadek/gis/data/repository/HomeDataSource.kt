package com.kadek.gis.data.repository

import androidx.lifecycle.LiveData
import com.kadek.gis.data.model.Area
import com.kadek.gis.data.model.Master
import com.kadek.gis.data.model.Section
import com.kadek.gis.data.model.Weather

interface HomeDataSource {

    fun getMaster(): LiveData<List<Master>>

    fun getPlantationGroup(): LiveData<List<Area>>

    fun getAreas(pgId: Int): LiveData<List<Area>>

    fun getLocations(pgId: Int, areaId: Int): LiveData<List<Area>>

    fun getSections(pgId: Int, areaId: Int, locationId: Int): LiveData<List<Area>>

    fun getDetailSection(sectionId: Long): LiveData<Section>

    fun getWeather(lat: Double, long: Double): LiveData<List<Weather?>>
}