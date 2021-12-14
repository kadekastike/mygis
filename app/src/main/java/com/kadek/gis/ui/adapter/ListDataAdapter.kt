package com.kadek.gis.ui.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kadek.gis.data.model.Maps
import com.kadek.gis.databinding.ItemListDataBinding
import com.kadek.gis.ui.layer.LayerNDVI
import com.kadek.gis.ui.layer.LayerTaksasi
import com.kadek.gis.ui.layer.LayerWeather
import com.kadek.gis.ui.layer.ProgressActivity

class ListDataAdapter : RecyclerView.Adapter<ListDataAdapter.ListDataViewHolder>() {

    private var listMaps = ArrayList<Maps>()

    fun setMaps(maps: List<Maps>?) {
        if (maps == null) return
        this.listMaps.clear()
        this.listMaps.addAll(maps)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListDataViewHolder {
        val dataListDataBinding = ItemListDataBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListDataViewHolder(dataListDataBinding)
    }

    override fun onBindViewHolder(holder: ListDataViewHolder, position: Int) {
        val map = listMaps[position]
        holder.bind(map)
    }

    override fun getItemCount(): Int = listMaps.size

    inner class ListDataViewHolder(private val binding: ItemListDataBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("NotifyDataSetChanged")
        fun bind(map: Maps) {
            with(binding) {

                title.text = map.name

                binding.expandableLayout.visibility = if (map.expand) View.VISIBLE else View.GONE
                binding.cardView.setOnClickListener {
                    map.expand = !map.expand
                    notifyDataSetChanged()
                }
                binding.taksasiButton.setOnClickListener {
                    val intent = Intent(binding.taksasiButton.context, LayerTaksasi::class.java)
                    intent.putExtra(LayerTaksasi.EXTRA_DATA, map.id)
                    binding.taksasiButton.context.startActivity(intent)
                }
                binding.ndviButton.setOnClickListener {
                    val intent = Intent(binding.ndviButton.context, LayerNDVI::class.java)
                    intent.putExtra(LayerNDVI.EXTRA_DATA, map.id)
                    binding.ndviButton.context.startActivity(intent)
                }
                binding.progressButton.setOnClickListener {
                    val intent = Intent(binding.progressButton.context, ProgressActivity::class.java)
                    intent.putExtra(ProgressActivity.EXTRA_DATA, map.id)
                    binding.progressButton.context.startActivity(intent)
                }
                binding.weatherButton.setOnClickListener {
                    val intent = Intent(binding.weatherButton.context, LayerWeather::class.java)
                    intent.putExtra(LayerWeather.EXTRA_DATA, map.id)
                    binding.weatherButton.context.startActivity(intent)
                }
            }
        }
    }
}