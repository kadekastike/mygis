package com.kadek.gis.ui

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.kadek.gis.R
import com.kadek.gis.databinding.ActivityListDataBinding
import com.kadek.gis.utils.ViewModelFactory
import com.kadek.gis.viewmodel.MainViewModel

class ListDataActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListDataBinding

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityListDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory = ViewModelFactory.getInstance()
        val viewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]

        val listDataAdapter = ListDataAdapter()

        binding.rvData.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = listDataAdapter
        }

        binding.progressBar2.visibility = View.VISIBLE
        viewModel.getMaps().observe(this, {
            binding.progressBar2.visibility = View.GONE
            listDataAdapter.setMaps(it)
            listDataAdapter.notifyDataSetChanged()
            Log.d("ListData", it.toString())
        })

    }
}