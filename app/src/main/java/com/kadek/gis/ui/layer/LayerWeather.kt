package com.kadek.gis.ui.layer

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.kadek.gis.R
import com.kadek.gis.databinding.ActivityLayerWeatherBinding
import com.kadek.gis.ui.adapter.WeatherListAdapter
import com.kadek.gis.utils.ViewModelFactory
import com.kadek.gis.viewmodel.MainViewModel
import com.kadek.gis.viewmodel.WeatherViewModel
import java.net.MalformedURLException
import java.net.URL
import java.util.*

class LayerWeather : AppCompatActivity(), OnMapReadyCallback {

    companion object {
        const val EXTRA_DATA = "extra_data"
    }

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityLayerWeatherBinding
    private lateinit var prepTiles: TileOverlay

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLayerWeatherBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val factory = ViewModelFactory.getInstance()
        val viewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]

        val dataId = intent.getIntExtra(EXTRA_DATA, 0)

//        binding.progressBar.bringToFront()
//        binding.progressBar.visibility = View.VISIBLE
        viewModel.getMapDetail(dataId).observe(this, { map ->

            val newarkLatLng = LatLng(map.sw_latitude, map.sw_longitude)
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newarkLatLng, 11f))

            val weatherViewModel = ViewModelProvider(this, factory)[WeatherViewModel::class.java]

            weatherViewModel.getWeather(map.sw_latitude, map.sw_longitude)

            weatherViewModel.currentHumidity.observe(this, {
                binding.humidityLevel.text = it.humidity.toString() + "%"
            })
            weatherViewModel.currentWeather.observe(this, {
                it.map {
                    binding.resWeather.text = it?.description
                    val baseUrl = "http://openweathermap.org/img/wn/"
                    Glide.with(this)
                        .load(baseUrl + it?.icon + "@2x.png")
                        .into(binding.imgWeather)
                }
            })

            val listWeahterAdapter = WeatherListAdapter()

            binding.rvWeather.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                setHasFixedSize(true)
                adapter = listWeahterAdapter
            }

            viewModel.getWeather(map.sw_latitude, map.sw_longitude).observe(this, {
                Log.d("WeatherViewModel", it.toString())
                listWeahterAdapter.setWeather(it)
                listWeahterAdapter.notifyDataSetChanged()
            })
        })

        val tileProvider: TileProvider = object : UrlTileProvider(256, 256) {
            @Synchronized
            override fun getTileUrl(x: Int, y: Int, z: Int): URL? {
                val mapUrl = "https://tile.openweathermap.org/map/precipitation/%d/%d/%d.png?appid=8c1c230db9d1ef99bf33660785575a51"
                val s = String.format(Locale.US, mapUrl, z, x, y)

                var url: URL? = null
                url = try {
                    URL(s)
                } catch (e: MalformedURLException) {
                    throw AssertionError(e)
                }
                return url
            }
        }
        Glide.with(this)
            .load(R.drawable.legend3)
            .into(binding.legend)
        prepTiles = mMap.addTileOverlay(TileOverlayOptions().tileProvider(tileProvider))!!

        BottomSheetBehavior.from(binding.bottomSheet).apply {
            peekHeight = 80
            this.state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }
}