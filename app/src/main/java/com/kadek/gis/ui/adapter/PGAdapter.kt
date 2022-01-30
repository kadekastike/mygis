package com.kadek.gis.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kadek.gis.data.model.Area
import com.kadek.gis.databinding.ItemListAreaBinding
import com.kadek.gis.ui.area.ListAreaActivity

class PGAdapter : RecyclerView.Adapter<PGAdapter.PGViewHolder>(){

    private val listAreas = ArrayList<Area>()

    fun setArea(areas: List<Area>?) {
        if (areas == null) return
        this.listAreas.clear()
        this.listAreas.addAll(areas)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PGAdapter.PGViewHolder {
        val areaListBinding = ItemListAreaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PGViewHolder(areaListBinding)
    }

    override fun onBindViewHolder(holder: PGAdapter.PGViewHolder, position: Int) {
        val area = listAreas[position]
        holder.bind(area)
    }

    override fun getItemCount(): Int = listAreas.size

    inner class PGViewHolder(private val binding: ItemListAreaBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(area : Area) {
            with(binding) {
                areaName.text = area.name

                itemView.setOnClickListener {
                    val intent = Intent(itemView.context, ListAreaActivity::class.java)
                    intent.putExtra(ListAreaActivity.EXTRA_DATA, area.pg)
                    intent.putExtra(ListAreaActivity.EXTRA_NAME, area.name)
                    itemView.context.startActivity(intent)
                }
            }
        }
    }
}