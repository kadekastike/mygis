package com.kadek.gis.ui

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.kadek.gis.databinding.ActivityMainBinding
import com.kadek.gis.ui.adapter.AreaAdapter
import com.kadek.gis.utils.ViewModelFactory
import com.kadek.gis.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory = ViewModelFactory.getInstance()
        val viewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]

        val listAreaAdapter = AreaAdapter()

        binding.rvArea.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = listAreaAdapter
        }

        binding.progressBar.visibility = View.VISIBLE
        viewModel.getAreas().observe(this, {
            binding.progressBar.visibility = View.GONE
            listAreaAdapter.setArea(it)
            listAreaAdapter.notifyDataSetChanged()
        })

    }
}