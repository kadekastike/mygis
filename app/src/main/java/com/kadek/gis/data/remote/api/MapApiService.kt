package com.kadek.gis.data.remote.api

import com.kadek.gis.data.remote.response.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MapApiService {

    @GET("map")
    fun getMaster(): Call<List<MasterResponseItem>>

    @GET("map/plantation-group")
    fun getPlantationGroup(): Call<DataResponse<ListAreaResponse>>

    @GET("map/area")
    fun getAreas(
        @Query("pg") pg: Int
    ): Call<DataResponse<ListAreaResponse>>

    @GET("map/location")
    fun getLocations(
        @Query("pg") pg: Int,
        @Query("area") area: Int
    ) : Call<DataResponse<ListAreaResponse>>

    @GET("map/section")
    fun getSections(
        @Query("pg") pg: Int,
        @Query("area") area: Int,
        @Query("location") location: Int
    ) : Call<DataResponse<ListAreaResponse>>

    @GET("map/section/{section_id}")
    fun getDetailSection(
        @Path("section_id") sectionId: Long
    ) : Call<SectionResponse>
}