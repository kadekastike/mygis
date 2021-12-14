package com.kadek.gis.data.remote.api

import com.kadek.gis.data.remote.response.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MapApiService {

    @GET("area")
    fun getAreas(): Call<DataResponse<AreaResponse>>

    @GET("map")
    fun getMaps(
        @Query("area") area: Int
    ) : Call<DataResponse<MapResponse>>

    @GET("map/{map_id}")
    fun getMapDetail(
        @Path("map_id") id: Int
    ) : Call<MapResponse>

}