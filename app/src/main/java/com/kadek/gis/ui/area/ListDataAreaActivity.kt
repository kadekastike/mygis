package com.kadek.gis.ui.area

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.kadek.gis.databinding.ActivityListDataAreaBinding
import com.kadek.gis.ui.adapter.ListDataAdapter
import com.kadek.gis.utils.ViewModelFactory
import com.kadek.gis.viewmodel.MainViewModel

class ListDataAreaActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_DATA = "extra_data"
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

        val listDataAreaAdapter = ListDataAdapter()

        binding.listArea.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = listDataAreaAdapter
        }

        val dataId = intent.getIntExtra(EXTRA_DATA, 0)
        val dataName = intent.getStringExtra(EXTRA_NAME)

        supportActionBar?.title = "$dataName Area List"

        binding.progressBar.visibility = View.VISIBLE
        viewModel.getMaps(dataId).observe(this, {
            binding.progressBar.visibility = View.GONE
            listDataAreaAdapter.setMaps(it)
            listDataAreaAdapter.notifyDataSetChanged()
        })
    }
}