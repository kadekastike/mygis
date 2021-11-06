package com.kadek.gis.data.remote.response

import com.google.gson.annotations.SerializedName

data class DataResponse<T>(

    @field:SerializedName("success")
    val success: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("data")
    val data: List<T>? = null,
)