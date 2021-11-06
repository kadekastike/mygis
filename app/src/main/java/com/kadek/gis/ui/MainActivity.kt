package com.kadek.gis.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.kadek.gis.R
import com.kadek.gis.databinding.ActivityMainBinding
import com.kadek.gis.utils.ViewModelFactory
import com.kadek.gis.viewmodel.MainViewModel
import com.kadek.gis.viewmodel.WeatherViewModel
import java.net.MalformedURLException
import java.net.URL
import java.util.*
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    companion object {
        const val EXTRA_DATA = "extra_data"

    }

    private val images: MutableList<BitmapDescriptor> = ArrayList()
    private var groundOverlay: GroundOverlay? = null
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMainBinding
    private lateinit var prepTiles: TileOverlay
    private lateinit var pref: SharedPreferences
    private var currentEntry = 0
    var latitude: Double = 0.0
    var longitude: Double = 0.0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu1 -> {
                val intent = Intent(this, ListDataActivity::class.java)
                startActivity(intent)
                true
            } R.id.menu2 -> {
                val intent = Intent(this, SettingActivity::class.java)
                startActivity(intent)
                true
            }
            else -> true
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.setPadding(0, 100, 0, 0)
        val factory = ViewModelFactory.getInstance()
        val viewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]

        val dataId = intent.getIntExtra(EXTRA_DATA, 9)

        val executor = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())

        binding.progressBar.bringToFront()
        binding.progressBar.visibility = View.VISIBLE
        viewModel.getMapDetail(dataId).observe(this, { map ->
            latitude = map.sw_latitude
            longitude = map.sw_longitude
            val newarkBounds = LatLngBounds(
                LatLng(map.sw_latitude, map.sw_longitude), //south west (barat daya)
                LatLng(map.ne_latitude, map.ne_longitude) // north east (timur laut)
            )
            executor.execute {
                images.clear()
                val baseUrl = "http://ff98-116-206-42-127.ngrok.io/storage/"

                val requestOptions = RequestOptions().override(100)
                    .downsample(DownsampleStrategy.CENTER_INSIDE)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)

                val taksasi = Glide.with(this)
                    .asBitmap()
                    .load(baseUrl + map.gambar_taksasi)
                    .apply(requestOptions)
                    .submit()
                    .get()

                val ndvi = Glide.with(this)
                    .asBitmap()
                    .load(baseUrl + map.gambar_ndvi)
                    .apply(requestOptions)
                    .submit()
                    .get()

                images.add(BitmapDescriptorFactory.fromBitmap(taksasi))
                images.add(BitmapDescriptorFactory.fromBitmap(ndvi))

                val newarkLatLng = LatLng(map.sw_latitude, map.sw_longitude)


                handler.post {
                    groundOverlay = mMap.addGroundOverlay(GroundOverlayOptions()
                        .positionFromBounds(newarkBounds)
                        .image(images[currentEntry]).anchor(0f, 1f)
                    )
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newarkLatLng, 10f))
                    binding.progressBar.visibility = View.GONE
                }
            }
            val weatherViewModel = ViewModelProvider(this, factory)[WeatherViewModel::class.java]

            weatherViewModel.getWeather(map.sw_latitude, map.sw_longitude)

            weatherViewModel.humidity.observe(this, {
                    Toast.makeText(this, "Humidity: " + it.humidity.toString() + "%", Toast.LENGTH_LONG).show()
            })
            weatherViewModel.currentWeather.observe(this, {
                it.map {
                    Toast.makeText(this, it?.description, Toast.LENGTH_LONG).show()
                }
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
        pref = getSharedPreferences(SettingActivity.PREFS_NAME, Context.MODE_PRIVATE)
        val state = pref.getBoolean(SettingActivity.SHOW_PRECIPITATION, true)

        if (state) {
            Glide.with(this)
                .load(R.drawable.legend3)
                .into(binding.legend)
            prepTiles = mMap.addTileOverlay(TileOverlayOptions().tileProvider(tileProvider))!!
        }

    }

    fun switchImage(view: View){
        val overlay = groundOverlay ?: return
        currentEntry = (currentEntry + 1) % images.size
        Log.d("currentEntry", currentEntry.toString())
        overlay.setImage(images[currentEntry])
    }

}