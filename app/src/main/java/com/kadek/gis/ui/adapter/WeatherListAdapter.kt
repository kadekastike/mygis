package com.kadek.gis.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.amulyakhare.textdrawable.util.ColorGenerator
import com.bumptech.glide.Glide
import com.kadek.gis.data.model.Weather
import com.kadek.gis.databinding.ItemListWeatherBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class WeatherListAdapter () : RecyclerView.Adapter<WeatherListAdapter.ViewHolder>(){

    private val listWeather = ArrayList<Weather?>()

    fun setWeather(weather: List<Weather?>) {
        if (weather == null) return
        this.listWeather.clear()
        this.listWeather.addAll(weather)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WeatherListAdapter.ViewHolder {
        val dataListWeatherBinding = ItemListWeatherBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return ViewHolder(dataListWeatherBinding)
    }

    override fun onBindViewHolder(holder: WeatherListAdapter.ViewHolder, position: Int) {
        val weather = listWeather[position]
        holder.bind(weather)
        val generator = ColorGenerator.MATERIAL

        // generate random color
        val color = generator.randomColor
        holder.cardWeather.setCardBackgroundColor(color)
    }

    override fun getItemCount(): Int = listWeather.size

    class ViewHolder(private val binding: ItemListWeatherBinding) : RecyclerView.ViewHolder(binding.root) {
        var cardWeather: CardView = binding.cardWeather

        @SuppressLint("SetTextI18n", "SimpleDateFormat")
        fun bind(weather: Weather?) {
            with(binding) {
                val dt = weather?.dt!!.toLong()
                val formatDate = SimpleDateFormat("d MMM yy")
                val date = formatDate.format(Date(dt.times(1000)))
                binding.dt.text = date
                binding.humidityLevel.text = weather.humidity.toString() + " %"
                binding.weatherDescription.text = weather.desc

                Glide.with(binding.iconWeather.context)
                    .load("http://openweathermap.org/img/wn/" + weather.icon +"@2x.png")
                    .into(binding.iconWeather)
            }
        }
    }
}
