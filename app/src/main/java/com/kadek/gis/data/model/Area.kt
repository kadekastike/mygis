package com.kadek.gis.data.model

data class Area(
    var id: Long?,
    var name: String?,
    var pg: Int?,
    var area: Int?,
    var location: Int?,
    var section: Int?,
    var chief: String?,
    var url: String?,
    var expand: Boolean = false
)