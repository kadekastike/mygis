package com.kadek.gis.data.repository

import androidx.lifecycle.LiveData
import com.kadek.gis.data.model.Maps

interface HomeDataSource {
    fun getMaps(): LiveData<List<Maps>>

    fun getDetailMap(id: Int): LiveData<Maps>
}