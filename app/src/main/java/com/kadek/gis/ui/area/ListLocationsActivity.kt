package com.kadek.gis.ui.area

import android.annotation.SuppressLint
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.kadek.gis.databinding.ActivityListDataAreaBinding
import com.kadek.gis.ui.adapter.AreaAdapter
import com.kadek.gis.ui.adapter.LocationAdapter
import com.kadek.gis.utils.ViewModelFactory
import com.kadek.gis.viewmodel.MainViewModel

class ListLocationsActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_DATA_PG = "extra_data"
        const val EXTRA_DATA_AREA = "extra_data_area"
        const val EXTRA_NAME = "extra_name"
    }
    private lateinit var binding: ActivityListDataAreaBinding

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityListDataAreaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory = ViewModelFactory.getInstance()
        val viewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]

        val locationAdapter = LocationAdapter()

        binding.listArea.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = locationAdapter
        }

        val pgId = intent.getIntExtra(EXTRA_DATA_PG, 0)
        val areaId = intent.getIntExtra(EXTRA_DATA_AREA, 0)
        val dataName = intent.getStringExtra(EXTRA_NAME)

        supportActionBar?.title = "$dataName Location List"

        binding.progressBar.visibility = View.VISIBLE
        viewModel.getLocations(pgId, areaId).observe(this, {
            binding.progressBar.visibility = View.GONE
            locationAdapter.setArea(it)
            locationAdapter.notifyDataSetChanged()
        })
    }
}