package com.kadek.gis.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.kadek.gis.data.model.Area
import com.kadek.gis.data.model.Maps
import com.kadek.gis.data.model.Weather
import com.kadek.gis.data.repository.HomeDataRepository

class MainViewModel(private val homeDataRepository: HomeDataRepository) : ViewModel() {

    fun getAreas(): LiveData<List<Area>> = homeDataRepository.getAreas()

    fun getMaps(areaId: Int) : LiveData<List<Maps>> = homeDataRepository.getMaps(areaId)

    fun getMapDetail(id : Int): LiveData<Maps> = homeDataRepository.getDetailMap(id)

    fun getWeather(lat: Double, long: Double): LiveData<List<Weather?>> = homeDataRepository.getWeather(lat, long)
}