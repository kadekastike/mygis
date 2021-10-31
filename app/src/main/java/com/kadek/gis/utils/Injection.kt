package com.kadek.gis.utils

import com.kadek.gis.data.repository.HomeDataRepository
import com.kadek.gis.data.repository.RemoteDataSource

object Injection {
    fun provideHomeDataRepository() : HomeDataRepository {
        val remoteDataSource = RemoteDataSource.getInstance()
        return HomeDataRepository.getInstance(remoteDataSource)
    }
}