package com.kadek.gis.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.kadek.gis.data.model.Maps
import com.kadek.gis.data.repository.HomeDataRepository

class MainViewModel(private val homeDataRepository: HomeDataRepository) : ViewModel() {

    fun getMaps() : LiveData<List<Maps>> = homeDataRepository.getMaps()

    fun getMapDetail(id : Int): LiveData<Maps> = homeDataRepository.getDetailMap(id)
}