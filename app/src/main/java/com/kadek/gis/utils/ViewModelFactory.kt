package com.kadek.gis.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kadek.gis.data.repository.HomeDataRepository
import com.kadek.gis.viewmodel.MainViewModel

class ViewModelFactory(private val homeDataRepository: HomeDataRepository) :
    ViewModelProvider.NewInstanceFactory() {
    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(): ViewModelFactory = instance ?: synchronized(this) {
            instance ?: ViewModelFactory(Injection.provideHomeDataRepository())
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return when {
                modelClass.isAssignableFrom(MainViewModel::class.java) -> MainViewModel(homeDataRepository) as T
                else -> throw  Throwable("Unknown ViewModel" + modelClass.name)
            }

        }
    }