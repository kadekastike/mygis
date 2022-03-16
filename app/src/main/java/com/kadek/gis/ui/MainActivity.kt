package com.kadek.gis.ui

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.PopupMenu
import androidx.core.view.get
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.kadek.gis.R
import com.kadek.gis.databinding.ActivityMainBinding
import com.kadek.gis.ui.adapter.MasterAdapter
import com.kadek.gis.ui.adapter.PGAdapter
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

        val listAreaAdapter = PGAdapter()

        binding.rvArea.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = listAreaAdapter
        }

        viewModel.getPlantationGroup().observe(this) {
            listAreaAdapter.setArea(it)
            listAreaAdapter.notifyDataSetChanged()
        }

        val listMasterAdapter = MasterAdapter()
        binding.rvMaster.apply {
            layoutManager = GridLayoutManager(context, 2)
            setHasFixedSize(true)
            adapter = listMasterAdapter
        }
        viewModel.getMaster().observe(this) {
            listMasterAdapter.setMaster(it)
            listMasterAdapter.notifyDataSetChanged()
        }
    }
//    private fun performOptionMenuClick(position: Int) {
//        Log.d("OptionMENu", position.toString())
//        val popupMenu = PopupMenu(this, binding.rvArea[position].findViewById(R.id.textViewOptions))
//        popupMenu.inflate(R.menu.option_menu_pg)
//        popupMenu.setOnMenuItemClickListener (object : PopupMenu.OnMenuItemClickListener {
//            override fun onMenuItemClick(item: MenuItem?): Boolean {
//                when(item?.itemId) {
//                    R.id.taksasi -> {
//                        val intent = Intent(this@MainActivity, PGMapActivity::class.java)
//                        startActivity(intent)
//                        return true
//                    }
//                    else -> {
//                        return false
//                    }
//                }
//            }
//        })
//        popupMenu.show()
//    }

}