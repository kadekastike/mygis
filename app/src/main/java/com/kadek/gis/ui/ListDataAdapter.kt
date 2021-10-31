package com.kadek.gis.ui

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kadek.gis.data.model.Maps
import com.kadek.gis.databinding.ItemListDataBinding

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
        fun bind(map: Maps) {
            with(binding) {
                title.text = map.name

                itemView.setOnClickListener {
                    val intent = Intent(itemView.context, MainActivity::class.java)
                    intent.putExtra(MainActivity.EXTRA_DATA, map.id)
                    itemView.context.startActivity(intent)
                }
            }
        }
    }
}