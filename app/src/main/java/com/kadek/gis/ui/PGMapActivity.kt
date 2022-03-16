package com.kadek.gis.ui

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.data.geojson.GeoJsonLayer
import com.kadek.gis.R
import com.kadek.gis.databinding.ActivityPgmapBinding
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONObject


class PGMapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityPgmapBinding
    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPgmapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val client = AsyncHttpClient()
        val url = "https://mygis.my.id/api/map/plantation-group/2202101003"
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?
            ) {
                val result = responseBody?.let { String(it) }

                try {
                    val responseObject = JSONObject(result)
                    val dataArray = responseObject.getJSONObject("data")
                    val geometryArray = dataArray.getJSONObject("geometry")

                    val geoJsonData: JSONObject = geometryArray

                    val jsonArray = geometryArray.getJSONArray("features")
                    val geo = jsonArray.getJSONObject(0).getJSONObject("geometry")
                    val coordinates = geo.getJSONArray("coordinates")
                    val listdata = ArrayList<LatLng>()

                    for (i in 0 until coordinates.length()) {
                        val coord = coordinates.toString().split(",")
                        val x = coord[0].toDouble()
                        val y = coord[1].toDouble()
                        listdata.add(LatLng(x, y))
                    }

                    val layer = GeoJsonLayer(mMap, geoJsonData)

                    for (feature in layer.features) {
                        layer.defaultPolygonStyle.fillColor = Color.parseColor(feature.getProperty("color"))
                        layer.addLayerToMap()
                    }

                    val bounds = getPolygonBounds(listdata)
                    Log.d("bounds", bounds.toString())
                    mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 10))

                    layer.setOnFeatureClickListener { feature ->
//                        val dialog = CustomDialogFragment(
//                            feature.getProperty("catatan"),
//                            feature.getProperty("pj"),
//                            feature.getProperty("created_at")
//                        )
//                        dialog.show(supportFragmentManager, "CustomDialog")
                        Toast.makeText(this@PGMapActivity, "clicked", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error?.message}"
                }
                Toast.makeText(this@PGMapActivity, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })

    }
    private fun getPolygonBounds(polygonPointsList: ArrayList<LatLng>): LatLngBounds {
        val builder = LatLngBounds.Builder()
        for (i in 0 until polygonPointsList.size) {
            builder.include(polygonPointsList[i])
        }
        return builder.build()
    }

}