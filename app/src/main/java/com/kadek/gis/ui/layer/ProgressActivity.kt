package com.kadek.gis.ui.layer

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.maps.android.data.geojson.GeoJsonLayer
import com.kadek.gis.R
import com.kadek.gis.databinding.ActivityProgressBinding
import com.kadek.gis.ui.CustomDialogFragment
import com.kadek.gis.utils.ViewModelFactory
import com.kadek.gis.viewmodel.MainViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject
import java.lang.Exception
import java.util.concurrent.Executors

class ProgressActivity : AppCompatActivity(), OnMapReadyCallback {

    companion object {
        const val EXTRA_DATA = "extra_data"
    }

    private var groundOverlay: GroundOverlay? = null
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityProgressBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProgressBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    @SuppressLint("SetTextI18n")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val factory = ViewModelFactory.getInstance()
        val viewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]

        val sectionId = intent.getLongExtra(EXTRA_DATA, 3)

        val executor = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())

        binding.progressBar.bringToFront()
        binding.progressBar.visibility = View.VISIBLE
        viewModel.getDetailSection(sectionId).observe(this, { map ->
            val newarkBounds = LatLngBounds(
                LatLng(map.sw_latitude, map.sw_longitude), //south west (barat daya)
                LatLng(map.ne_latitude, map.ne_longitude) // north east (timur laut)
            )
            executor.execute {

                val taksasi = getImage(map.gambar_taksasi)
                val newarkLatLng = LatLng(map.sw_latitude, map.sw_longitude)

                handler.post {
                    supportActionBar?.title = map.name + " Job Progress"
                    groundOverlay = mMap.addGroundOverlay(
                        GroundOverlayOptions()
                            .positionFromBounds(newarkBounds)
                            .image(BitmapDescriptorFactory.fromBitmap(taksasi)).anchor(1f, 0f)
                    )
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newarkLatLng, 10f))
                    binding.progressBar.visibility = View.GONE
                }
            }
        })

        val client = AsyncHttpClient()
        val url = "https://mygis.coejtm-unila.com/data/geojson/$sectionId"
        client.get(url, object : AsyncHttpResponseHandler(){
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?
            ) {
                val result = responseBody?.let { String(it) }

                    try {
                        val responseObject = JSONObject(result)

                        val geoJsonData: JSONObject = responseObject
                        val layer = GeoJsonLayer(mMap, geoJsonData)

                        layer.defaultPolygonStyle.fillColor = Color.rgb(65, 111, 181)
                        layer.defaultPolygonStyle.strokeColor = Color.rgb(65, 111, 181)
                        layer.addLayerToMap()

                        layer.setOnFeatureClickListener { feature ->
                            val dialog = CustomDialogFragment(feature.getProperty("catatan"), feature.getProperty("pj"), feature.getProperty("created_at"))
                            dialog.show(supportFragmentManager, "CustomDialog")
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
                Toast.makeText(this@ProgressActivity, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getImage(address: String) : Bitmap {

        val baseUrl = "https://mygis.coejtm-unila.com/storage/"

        return Glide.with(this)
            .asBitmap()
            .load(baseUrl + address)
            .apply(RequestOptions().override(200,200))
            .submit()
            .get()
    }
}