package com.kadek.gis.data.model

data class Section(
    var id: Long?,
    var name: String?,
    var sw_latitude: Double,
    var sw_longitude: Double,
    var ne_latitude: Double,
    var ne_longitude: Double,
    var gambar_taksasi: String,
    var gambar_ndvi: String,
    var age: String?,
    var variety: String?,
    var crop: String?,
    var forcing_time: Int?,
    var created_at: String?,
    var updated_at: String?,
    var expand: Boolean = false
)

