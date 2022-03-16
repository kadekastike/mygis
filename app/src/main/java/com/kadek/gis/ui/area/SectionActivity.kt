package com.kadek.gis.ui.area

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.core.view.get
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.kadek.gis.R
import com.kadek.gis.data.model.Area
import com.kadek.gis.databinding.ActivitySectionBinding
import com.kadek.gis.ui.adapter.SectionAdapter
import com.kadek.gis.ui.layer.*
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
    private var sectionList = ArrayList<Area>()

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory = ViewModelFactory.getInstance()
        val viewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]

        val sectionAdapter = SectionAdapter(sectionList, object : SectionAdapter.OptionsMenuClickListener{
            override fun optionsMenuClicked(position: Int) {
                performOptionMenuClick(position)
            }
        })

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
        viewModel.getSections(pgId, areaId, locationId).observe(this) {
            binding.progressBar.visibility = View.GONE
            sectionAdapter.setSection(it)
            sectionList.addAll(it)
            sectionAdapter.notifyDataSetChanged()
        }

    }
    private fun performOptionMenuClick(position: Int) {
        Log.d("OptionMENu", position.toString())
        val popupMenu = PopupMenu(this, binding.listArea[position].findViewById(R.id.textViewOptions))
        popupMenu.inflate(R.menu.option_menu)
        popupMenu.setOnMenuItemClickListener (object : PopupMenu.OnMenuItemClickListener {
            override fun onMenuItemClick(item: MenuItem?): Boolean {
                when(item?.itemId) {
                    R.id.taksasi -> {
                        val intent = Intent(this@SectionActivity, LayerTaksasi::class.java)
                        intent.putExtra(LayerTaksasi.EXTRA_DATA, sectionList[position].id)
                        startActivity(intent)
                        return true
                    }
                    R.id.ndvi -> {
                        val intent = Intent(this@SectionActivity, LayerNDVI::class.java)
                        intent.putExtra(LayerNDVI.EXTRA_DATA, sectionList[position].id)
                        startActivity(intent)
                        return true
                    }
                    R.id.job_progress -> {
                        val intent = Intent(this@SectionActivity, ProgressActivity::class.java)
                        intent.putExtra(ProgressActivity.EXTRA_DATA, sectionList[position].id)
                        startActivity(intent)
                        return true
                    }
                    R.id.irrigation -> {
                        val intent = Intent(this@SectionActivity, IrrigationActivity::class.java)
                        intent.putExtra(IrrigationActivity.EXTRA_DATA, sectionList[position].id)
                        startActivity(intent)
                        return true
                    }
                    else -> {
                        return false
                    }
                }
            }
        })
        popupMenu.show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        this.finish()
    }
}