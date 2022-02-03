package com.kadek.gis.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kadek.gis.data.model.Master
import com.kadek.gis.databinding.ItemMasterBinding

class MasterAdapter: RecyclerView.Adapter<MasterAdapter.MasterViewHolder>() {

    private val listMaster = ArrayList<Master>()

    fun setMaster(master: List<Master>?) {
        if (master == null) return
        this.listMaster.clear()
        this.listMaster.addAll(master)
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MasterAdapter.MasterViewHolder {
        val masterBinding = ItemMasterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MasterViewHolder(masterBinding)
    }

    override fun onBindViewHolder(holder: MasterAdapter.MasterViewHolder, position: Int) {
        val master = listMaster[position]
        holder.bind(master)
    }

    override fun getItemCount(): Int = listMaster.size

    inner class MasterViewHolder(private val binding: ItemMasterBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(master: Master) = with(binding) {
            binding.title.text = master.title
            binding.total.text = master.total.toString()
        }
    }
}