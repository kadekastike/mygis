package com.kadek.gis.ui.area

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.kadek.gis.databinding.ActivitySectionBinding
import com.kadek.gis.ui.adapter.SectionAdapter
import com.kadek.gis.utils.ViewModelFactory
import com.kadek.gis.viewmodel.MainViewModel

class SectionActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_DATA_PG = "extra_data"
        const val EXTRA_DATA_AREA = "extra_data_area"
        const val EXTRA_DATA_LOCATION = "extra_data_location"
        const val EXTRA_NAME = "extra_name"
    }
    private lateinit var binding: ActivitySectionBinding

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory = ViewModelFactory.getInstance()
        val viewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]

        val sectionAdapter = SectionAdapter()

        binding.listArea.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = sectionAdapter
        }

        val pgId = intent.getIntExtra(EXTRA_DATA_PG, 0)
        val areaId = intent.getIntExtra(EXTRA_DATA_AREA, 0)
        val locationId = intent.getIntExtra(EXTRA_DATA_LOCATION, 0)
        val dataName = intent.getStringExtra(EXTRA_NAME)

        supportActionBar?.title = "$dataName Section List"

        binding.progressBar.visibility = View.VISIBLE
        viewModel.getSections(pgId, areaId, locationId).observe(this, {
            binding.progressBar.visibility = View.GONE
            sectionAdapter.setSection(it)
            sectionAdapter.notifyDataSetChanged()
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        this.finish()
    }
}