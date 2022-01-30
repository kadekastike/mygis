package com.kadek.gis.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kadek.gis.data.model.Area
import com.kadek.gis.databinding.ItemListAreaBinding
import com.kadek.gis.ui.area.ListAreaActivity
import com.kadek.gis.ui.area.ListLocationsActivity
import com.kadek.gis.ui.area.SectionActivity

class LocationAdapter : RecyclerView.Adapter<LocationAdapter.LocationViewHolder>(){

    private val listAreas = ArrayList<Area>()

    fun setArea(areas: List<Area>?) {
        if (areas == null) return
        this.listAreas.clear()
        this.listAreas.addAll(areas)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationAdapter.LocationViewHolder {
        val areaListBinding = ItemListAreaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LocationViewHolder(areaListBinding)
    }

    override fun onBindViewHolder(holder: LocationAdapter.LocationViewHolder, position: Int) {
        val area = listAreas[position]
        holder.bind(area)
    }

    override fun getItemCount(): Int = listAreas.size

    inner class LocationViewHolder(private val binding: ItemListAreaBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(area : Area) {
            with(binding) {
                areaName.text = area.name

                itemView.setOnClickListener {
                    val intent = Intent(itemView.context, SectionActivity::class.java)
                    intent.putExtra(SectionActivity.EXTRA_DATA_PG, area.pg)
                    intent.putExtra(SectionActivity.EXTRA_DATA_AREA, area.area)
                    intent.putExtra(SectionActivity.EXTRA_DATA_LOCATION, area.location)
                    intent.putExtra(SectionActivity.EXTRA_NAME, area.name)
                    itemView.context.startActivity(intent)
                }
            }
        }
    }
}