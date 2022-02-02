package com.kadek.gis.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kadek.gis.data.model.Area
import com.kadek.gis.databinding.ItemListAreaBinding
import com.kadek.gis.ui.area.ListLocationsActivity

class AreaAdapter : RecyclerView.Adapter<AreaAdapter.AreaViewHolder>(){

    private val listAreas = ArrayList<Area>()
 
    fun setArea(areas: List<Area>?) {
        if (areas == null) return
        this.listAreas.clear()
        this.listAreas.addAll(areas)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AreaAdapter.AreaViewHolder {
        val areaListBinding = ItemListAreaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AreaViewHolder(areaListBinding)
    }

    override fun onBindViewHolder(holder: AreaAdapter.AreaViewHolder, position: Int) {
        val area = listAreas[position]
        holder.bind(area)
    }

    override fun getItemCount(): Int = listAreas.size

    inner class AreaViewHolder(private val binding: ItemListAreaBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(area : Area) {
            with(binding) {
                areaName.text = area.name
                createdAt.text = area.created_at
                chief.text = area.chief

                itemView.setOnClickListener {
                    val intent = Intent(itemView.context, ListLocationsActivity::class.java)
                    intent.putExtra(ListLocationsActivity.EXTRA_DATA_PG, area.pg)
                    intent.putExtra(ListLocationsActivity.EXTRA_DATA_AREA, area.area)
                    intent.putExtra(ListLocationsActivity.EXTRA_NAME, area.name)
                    itemView.context.startActivity(intent)
                }
            }
        }
    }
}