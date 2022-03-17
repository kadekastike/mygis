package com.kadek.gis.ui.layer

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.kadek.gis.R
import com.kadek.gis.databinding.ActivityLayerNdviBinding
import com.kadek.gis.utils.Helper.getImage
import com.kadek.gis.utils.ViewModelFactory
import com.kadek.gis.viewmodel.MainViewModel
import java.util.concurrent.Executors

class LayerNDVI : AppCompatActivity(), OnMapReadyCallback {

    companion object {
        const val EXTRA_DATA = "extra_data"
    }

    private var groundOverlay: GroundOverlay? = null
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityLayerNdviBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLayerNdviBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.dataNotFound.visibility = View.GONE
        binding.ndviLegend.visibility = View.GONE
        binding.map.visibility = View.GONE
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val factory = ViewModelFactory.getInstance()
        val viewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]

        val sectionId = intent.getLongExtra(EXTRA_DATA, 0)

        val executor = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())

        binding.progressBar.bringToFront()
        binding.progressBar.visibility = View.VISIBLE

        viewModel.getDetailSection(sectionId).observe(this) { section ->
            if (section.gambar_ndvi != null) {
                binding.ndviLegend.visibility = View.VISIBLE
                binding.map.visibility = View.VISIBLE
                val newarkBounds = LatLngBounds(
                    LatLng(section.sw_latitude!!, section.sw_longitude!!), //south west (barat daya)
                    LatLng(section.ne_latitude!!, section.ne_longitude!!) // north east (timur laut)
                )
                executor.execute {

                    val taksasi = getImage(this, section.gambar_ndvi!!)
                    val newarkLatLng = LatLng(section.sw_latitude!!, section.sw_longitude!!)

                    handler.post {
                        supportActionBar?.title = section.name + " Taksasi"
                        groundOverlay = mMap.addGroundOverlay(
                            GroundOverlayOptions()
                                .positionFromBounds(newarkBounds)
                                .image(BitmapDescriptorFactory.fromBitmap(taksasi)).anchor(1f, 0f)
                        )
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newarkLatLng, 16f))
                        binding.progressBar.visibility = View.GONE
                    }
                }
            } else {
                binding.map.visibility = View.GONE
                binding.ndviLegend.visibility = View.GONE
                binding.progressBar.visibility = View.GONE
                binding.dataNotFound.visibility = View.VISIBLE
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
