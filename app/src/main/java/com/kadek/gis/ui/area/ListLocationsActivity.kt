package com.kadek.gis.ui.area

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.kadek.gis.databinding.ActivityListDataAreaBinding
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

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

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

        binding.dataNotFound.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
        viewModel.getLocations(pgId, areaId).observe(this) {
            Log.d("data", it.toString())
            binding.progressBar.visibility = View.GONE

            Log.d("dataArea", locationAdapter.setArea(it).toString())
            if (it.isEmpty()) {
                binding.listArea.visibility = View.GONE
                binding.dataNotFound.visibility = View.VISIBLE
            } else {
                locationAdapter.setArea(it)
                locationAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}