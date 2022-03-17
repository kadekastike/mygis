package com.kadek.gis.ui.layer

import android.annotation.SuppressLint
import android.graphics.Bitmap
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
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.kadek.gis.R
import com.kadek.gis.databinding.ActivityLayerTaksasiBinding
import com.kadek.gis.utils.Helper.getImage
import com.kadek.gis.utils.ViewModelFactory
import com.kadek.gis.viewmodel.MainViewModel
import java.util.concurrent.Executors

class LayerTaksasi : AppCompatActivity(), OnMapReadyCallback {

    companion object {
        const val EXTRA_DATA = "extra_data"
    }

    private var groundOverlay: GroundOverlay? = null
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityLayerTaksasiBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLayerTaksasiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.dataNotFound.visibility = View.GONE
        binding.bottomSheet.visibility = View.GONE
        binding.map.visibility = View.GONE
    }

    @SuppressLint("SetTextI18n")
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
            if (section.gambar_taksasi != null) {
                binding.bottomSheet.visibility = View.VISIBLE
                binding.map.visibility = View.VISIBLE
                val newarkBounds = LatLngBounds(
                    LatLng(section.sw_latitude!!, section.sw_longitude!!), //south west (barat daya)
                    LatLng(section.ne_latitude!!, section.ne_longitude!!) // north east (timur laut)
                )
                executor.execute {

                    val taksasi = getImage(this, section.gambar_taksasi!!)
                    val newarkLatLng = LatLng(section.sw_latitude!!, section.sw_longitude!!)

                    handler.post {
                        supportActionBar?.title = section.name + " Taksasi"
                        binding.ageResult.text = section.age
                        binding.cropResult.text = section.crop + " crop"
                        binding.varietyResult.text = section.variety
                        binding.ft.text = section.forcing_time.toString()

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
                binding.progressBar.visibility = View.GONE
                binding.bottomSheet.visibility = View.GONE
                binding.dataNotFound.visibility = View.VISIBLE
            }

        }
        BottomSheetBehavior.from(binding.bottomSheet).apply {
            peekHeight = 280
            this.state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

}