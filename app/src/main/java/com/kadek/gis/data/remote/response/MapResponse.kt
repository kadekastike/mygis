package com.kadek.gis.data.remote.response

import com.google.gson.annotations.SerializedName

data class MapResponse(

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("created_by")
	val createdBy: String,

	@field:SerializedName("sw_latitude")
	val swLatitude: Double,

	@field:SerializedName("sw_longitude")
	val swLongitude: Double,

	@field:SerializedName("ne_latitude")
	val neLatitude: Double,

	@field:SerializedName("ne_longitude")
	val neLongitude: Double,

	@field:SerializedName("gambar_taksasi")
	val gambarTaksasi: String,

	@field:SerializedName("gambar_ndvi")
	val gambarNdvi: String,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("updated_at")
	val updatedAt: String
)
