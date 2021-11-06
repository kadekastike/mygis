package com.kadek.gis.data.remote.api

import com.kadek.gis.data.remote.response.MapResponse
import com.kadek.gis.data.remote.response.DataResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface MapApiService {
    @GET("map")
    fun getMaps() : Call<DataResponse<MapResponse>>

    @GET("map/{map_id}")
    fun getMapDetail(
        @Path("map_id") id: Int,
    ) : Call<MapResponse>
}