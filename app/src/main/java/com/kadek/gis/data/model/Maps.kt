package com.kadek.gis.data.model

data class Maps(
    var id: Int,
    var name: String,
    var sw_latitude: Double,
    var sw_longitude: Double,
    var ne_latitude: Double,
    var ne_longitude: Double,
    var gambar_taksasi: String,
    var gambar_ndvi: String,
    var created_at: String,
    var updated_at: String
)

