package com.kadek.gis.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kadek.gis.data.model.Area
import com.kadek.gis.databinding.ItemListAreaBinding
import com.kadek.gis.ui.area.ListDataAreaActivity

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

                itemView.setOnClickListener {
                    val intent = Intent(itemView.context, ListDataAreaActivity::class.java)
                    intent.putExtra(ListDataAreaActivity.EXTRA_DATA, area.id)
                    intent.putExtra(ListDataAreaActivity.EXTRA_NAME, area.name)
                    itemView.context.startActivity(intent)
                }
            }
        }
    }
}