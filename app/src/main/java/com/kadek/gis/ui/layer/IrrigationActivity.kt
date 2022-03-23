package com.kadek.gis.ui.layer

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.maps.android.data.geojson.GeoJsonLayer
import com.kadek.gis.R
import com.kadek.gis.databinding.ActivityIrigationBinding
import com.kadek.gis.utils.Helper
import com.kadek.gis.utils.ViewModelFactory
import com.kadek.gis.viewmodel.MainViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject
import java.lang.Exception
import java.util.concurrent.Executors

class IrrigationActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityIrigationBinding
    private lateinit var mMap: GoogleMap
    private var groundOverlay: GroundOverlay? = null

    companion object {
        const val EXTRA_DATA = "EXTRA_DATA"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityIrigationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.dataNotFound.visibility = View.GONE
        binding.map.visibility = View.GONE
        binding.bottomSheet.visibility = View.GONE
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val factory = ViewModelFactory.getInstance()
        val viewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]

        val sectionId = intent.getLongExtra(EXTRA_DATA, 3)

        val executor = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())

        viewModel.getDetailSection(sectionId).observe(this) { map ->
            if (map.gambar_taksasi != null) {
                val newarkBounds = LatLngBounds(
                    LatLng(map.sw_latitude!!, map.sw_longitude!!), //south west (barat daya)
                    LatLng(map.ne_latitude!!, map.ne_longitude!!) // north east (timur laut)
                )
                executor.execute {
                    val taksasi = Helper.getImage(this, map.gambar_taksasi!!)
                    val newarkLatLng = LatLng(map.sw_latitude!!, map.sw_longitude!!)

                    handler.post {
                        supportActionBar?.title = map.name + " Irrigation"
                        groundOverlay = mMap.addGroundOverlay(
                            GroundOverlayOptions()
                                .positionFromBounds(newarkBounds)
                                .image(BitmapDescriptorFactory.fromBitmap(taksasi)).anchor(1f, 0f)
                        )
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newarkLatLng, 12f))
                    }
                }
            } else {
                showNoData()
            }

        }

        val client = AsyncHttpClient()
        val url = "https://mygis.coejtm-unila.com/api/map/section/$sectionId"
        client.get(url, object : AsyncHttpResponseHandler(){
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?
            ) {
                val result = responseBody?.let { String(it) }

                try {
                    val responseObject = JSONObject(result)
                    val dataObject = responseObject.getJSONObject("data")

                    val irrigationObject = dataObject.getJSONObject("irigation")
                    val name = irrigationObject.getString("name")
                    val volume = irrigationObject.getString("state")

                    binding.nameResult.text = name
                    binding.valumeResult.text = volume

                    val geometryData = irrigationObject.getJSONObject("geometry")
                    val geoJsonData: JSONObject = geometryData

                    val layer = GeoJsonLayer(mMap, geoJsonData)
                    when (volume) {
                        "full" -> {
                            layer.defaultPolygonStyle.fillColor = Color.rgb(30,129,176)
                        }
                        "half" -> {
                            layer.defaultPolygonStyle.fillColor = Color.rgb(65, 111, 181)
                        }
                        "quarter" -> {
                            layer.defaultPolygonStyle.fillColor = Color.rgb(255, 170, 5)
                        }
                        else -> {
                            layer.defaultPolygonStyle.fillColor = Color.rgb(255, 49, 5)
                        }
                    }

                    layer.defaultPolygonStyle.strokeColor = Color.rgb(65, 111, 181)
                    layer.addLayerToMap()
                    binding.bottomSheet.visibility = View.VISIBLE
                    binding.map.visibility = View.VISIBLE

                    BottomSheetBehavior.from(binding.bottomSheet).apply {
                        peekHeight = 280
                        this.state = BottomSheetBehavior.STATE_COLLAPSED
                    }

                } catch (e: Exception) {
                    showNoData()
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
                Toast.makeText(this@IrrigationActivity, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun showNoData() {
        binding.dataNotFound.visibility = View.VISIBLE
        binding.map.visibility = View.GONE
        binding.bottomSheet.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
    }
}