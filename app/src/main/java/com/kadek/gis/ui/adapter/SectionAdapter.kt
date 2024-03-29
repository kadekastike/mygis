package com.kadek.gis.ui.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.google.maps.android.data.Layer
import com.kadek.gis.R
import com.kadek.gis.data.model.Area
import com.kadek.gis.databinding.ItemListDataBinding
import com.kadek.gis.ui.layer.LayerNDVI
import com.kadek.gis.ui.layer.LayerTaksasi
import com.kadek.gis.ui.layer.LayerWeather
import com.kadek.gis.ui.layer.ProgressActivity

class SectionAdapter(
    private var sectionList: List<Area>,
    private var optionsMenuClickListener: OptionsMenuClickListener
) : RecyclerView.Adapter<SectionAdapter.ListDataViewHolder>() {

    private var listSection = ArrayList<Area>()

    fun setSection(section: List<Area>?) {
        if (section == null) return
        this.listSection.clear()
        this.listSection.addAll(section)
    }

    interface OptionsMenuClickListener {
        fun optionsMenuClicked(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListDataViewHolder {
        val dataListDataBinding = ItemListDataBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListDataViewHolder(dataListDataBinding)
    }

    override fun onBindViewHolder(holder: ListDataViewHolder, position: Int) {
        val section = listSection[position]
        holder.bind(section)
        holder.optionMenu.setOnClickListener {
            optionsMenuClickListener.optionsMenuClicked(position)
        }
        holder.cardView.setOnClickListener {
            optionsMenuClickListener.optionsMenuClicked(position)
        }
    }

    override fun getItemCount(): Int = listSection.size

    inner class ListDataViewHolder(private val binding: ItemListDataBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("NotifyDataSetChanged")
        val optionMenu = binding.textViewOptions
        val cardView = binding.cardView
        fun bind(section: Area) {
            with(binding) {

                title.text = section.name
                chief.text = section.chief
                createdAt.text = section.created_at

//                binding.expandableLayout.visibility = if (section.expand) View.VISIBLE else View.GONE
//                binding.cardView.setOnClickListener {
//                    section.expand = !section.expand
//                    notifyDataSetChanged()
//                }
//                binding.taksasiButton.setOnClickListener {
//                    val intent = Intent(binding.taksasiButton.context, LayerTaksasi::class.java)
//                    intent.putExtra(LayerTaksasi.EXTRA_DATA, section.id)
//                    binding.taksasiButton.context.startActivity(intent)
//                }
//                binding.ndviButton.setOnClickListener {
//                    val intent = Intent(binding.ndviButton.context, LayerNDVI::class.java)
//                    intent.putExtra(LayerNDVI.EXTRA_DATA, section.id)
//                    binding.ndviButton.context.startActivity(intent)
//                }
//                binding.progressButton.setOnClickListener {
//                    val intent = Intent(binding.progressButton.context, ProgressActivity::class.java)
//                    intent.putExtra(ProgressActivity.EXTRA_DATA, section.id)
//                    binding.progressButton.context.startActivity(intent)
//                }
//                binding.weatherButton.setOnClickListener {
//                    val intent = Intent(binding.weatherButton.context, LayerWeather::class.java)
//                    intent.putExtra(LayerWeather.EXTRA_DATA, section.id)
//                    binding.weatherButton.context.startActivity(intent)
//                }
            }
        }
    }
}