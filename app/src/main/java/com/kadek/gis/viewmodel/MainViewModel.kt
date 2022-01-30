package com.kadek.gis.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.kadek.gis.data.model.Area
import com.kadek.gis.data.model.Section
import com.kadek.gis.data.model.Weather
import com.kadek.gis.data.repository.HomeDataRepository

class MainViewModel(private val homeDataRepository: HomeDataRepository) : ViewModel() {

    fun getPlantationGroup(): LiveData<List<Area>> = homeDataRepository.getPlantationGroup()

    fun getAreas(pgId : Int): LiveData<List<Area>> = homeDataRepository.getAreas(pgId)

    fun getLocations(pgId: Int, areaId: Int): LiveData<List<Area>> = homeDataRepository.getLocations(pgId, areaId)

    fun getSections(pgId: Int, areaId: Int, locationId: Int): LiveData<List<Area>> = homeDataRepository.getSections(pgId, areaId, locationId)

    fun getDetailSection(sectionId : Long): LiveData<Section> = homeDataRepository.getDetailSection(sectionId)

    fun getWeather(lat: Double, long: Double): LiveData<List<Weather?>> = homeDataRepository.getWeather(lat, long)
}