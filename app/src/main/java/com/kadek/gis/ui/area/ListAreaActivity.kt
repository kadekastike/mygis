package com.kadek.gis.ui.area

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.kadek.gis.databinding.ActivityListAreaBinding
import com.kadek.gis.ui.adapter.AreaAdapter
import com.kadek.gis.utils.ViewModelFactory
import com.kadek.gis.viewmodel.MainViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class ListAreaActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_DATA = "extra_data"
        const val EXTRA_NAME = "extra_name"
    }
    private lateinit var binding: ActivityListAreaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityListAreaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val factory = ViewModelFactory.getInstance()
        val viewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]

        val listDataAreaAdapter = AreaAdapter()

        binding.listArea.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = listDataAreaAdapter
        }

        val dataId = intent.getIntExtra(EXTRA_DATA, 0)
        val dataName = intent.getStringExtra(EXTRA_NAME)

        supportActionBar?.title = "$dataName Detail"

        binding.dataNotFound.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
        viewModel.getAreas(dataId).observe(this) {
            binding.progressBar.visibility = View.GONE
            if (it.isEmpty()) {
                binding.listArea.visibility = View.GONE
                binding.title.visibility = View.GONE
                binding.dataNotFound.visibility = View.VISIBLE
            } else {
                listDataAreaAdapter.setArea(it)
                listDataAreaAdapter.notifyDataSetChanged()
            }
        }

        if (dataName == "PG - 3") {
            setLineChart()
        } else {
            binding.text.visibility = View.GONE
            binding.text2.visibility = View.GONE
            binding.text3.visibility = View.GONE
            binding.humCard.visibility = View.GONE
            binding.smcard.visibility = View.GONE
            binding.tempCard.visibility = View.GONE
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun setLineChart() {
        val soilMoistureN001 = ArrayList<Entry>()
        val soilMoistureN002 = ArrayList<Entry>()
        val soilMoistureN003 = ArrayList<Entry>()

        val humidityN001 = ArrayList<Entry>()
        val humidityN002 = ArrayList<Entry>()
        val humidityN003 = ArrayList<Entry>()

        val temperatureN001 = ArrayList<Entry>()
        val temperatureN002 = ArrayList<Entry>()
        val temperatureN003 = ArrayList<Entry>()

        val client = AsyncHttpClient()
        val url = "https://mygis.my.id/api/node/get"
        client.get(url, object : AsyncHttpResponseHandler(){
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?
            ) {
                val result = responseBody?.let { String(it) }
                try {
                    Log.d("result", result.toString())

                    val responseObject = JSONObject(result)
                    val soilMoistureArray = responseObject.getJSONArray("soil_moisture")

//                    for (i in 0 until soilMoistureArray.length()) {
//                        val dataArray = soilMoistureArray.getJSONObject(i).getJSONArray("data")
//                        Log.d("data", dataArray.toString())
//                        val label = soilMoistureArray.getJSONObject(i).getString("name")
//                        Log.d("label", label)
//                        for (j in 0 until dataArray.length()) {
//                            soilMoistureN001.add(Entry(j.toFloat(), dataArray.get(j).toString().toFloat()))
//                        }
//                        val soilMoistureDataSet = LineDataSet(soilMoistureN001, label)
//                        soilMoistureDataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
//                        soilMoistureDataSet.color = Color.BLUE
//
//                        binding.soilmoisture.data = LineData(soilMoistureDataSet)
//
//                    }
//                    binding.soilmoisture.xAxis.position = XAxis.XAxisPosition.BOTTOM
//                    binding.soilmoisture.description.isEnabled = false
//                    binding.soilmoisture.animateXY(100, 500)
                    val dataN001Array = soilMoistureArray.getJSONObject(0).getJSONArray("data")
                    for (i in 0 until dataN001Array.length()) {
                        soilMoistureN001.add(Entry(i.toFloat(), dataN001Array.get(i).toString().toFloat()))
                    }

                    val dataN002Array = soilMoistureArray.getJSONObject(1).getJSONArray("data")
                    for (i in 0 until dataN002Array.length()) {
                        soilMoistureN002.add(Entry(i.toFloat(), dataN002Array.get(i).toString().toFloat() ))
                    }

                    val soilMoistureN001DataSet = LineDataSet(soilMoistureN001, "N001")
                    soilMoistureN001DataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
                    soilMoistureN001DataSet.color = Color.GREEN
                    soilMoistureN001DataSet.lineWidth = 1.3f

                    val soilMoistureN002DataSet = LineDataSet(soilMoistureN002, "N002")
                    soilMoistureN002DataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
                    soilMoistureN002DataSet.lineWidth = 1.3f
                    soilMoistureN002DataSet.color = Color.BLUE

                    val legend = binding.soilmoisture.legend
                    legend.isEnabled = true

                    binding.soilmoisture.xAxis.position = XAxis.XAxisPosition.BOTTOM
                    binding.soilmoisture.description.isEnabled = false
                    binding.soilmoisture.data = LineData(soilMoistureN001DataSet, soilMoistureN002DataSet)
                    binding.soilmoisture.notifyDataSetChanged()
                    binding.soilmoisture.invalidate()
                    binding.soilmoisture.animateXY(100, 500)

                    // humidity line chart
                    val humidityArray = responseObject.getJSONArray("humidity")
                    val humidityN001Data = humidityArray.getJSONObject(0).getJSONArray("data")
                    for (i in 0 until humidityN001Data.length()) {
                        humidityN001.add(Entry(i.toFloat(), humidityN001Data.get(i).toString().toFloat()))
                    }
                    val humidityN002Data = humidityArray.getJSONObject(1).getJSONArray("data")
                    for (i in 0 until humidityN002Data.length()) {
                        humidityN002.add(Entry(i.toFloat(), humidityN002Data.get(i).toString().toFloat()))
                    }

                    val humidityN001DataSet = LineDataSet(humidityN001, "N001")
                    humidityN001DataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
                    humidityN001DataSet.lineWidth = 1.3f
                    humidityN001DataSet.color = Color.GREEN

                    val humidityN002DataSet = LineDataSet(humidityN002, "N002")
                    humidityN002DataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
                    humidityN002DataSet.lineWidth = 1.3f
                    humidityN002DataSet.color = Color.BLUE

                    val humidityLegend = binding.humidity.legend
                    humidityLegend.isEnabled = true

                    binding.humidity.xAxis.position = XAxis.XAxisPosition.BOTTOM
                    binding.humidity.notifyDataSetChanged()
                    binding.humidity.invalidate()
                    binding.humidity.description.isEnabled = false
                    binding.humidity.data = LineData(humidityN001DataSet, humidityN002DataSet)
                    binding.humidity.animateXY(100, 500)


                    // temperature
                    val temperatureArray = responseObject.getJSONArray("temp")
                    val tempN001Data = temperatureArray.getJSONObject(0).getJSONArray("data")
                    for (i in 0 until tempN001Data.length()){
                        temperatureN001.add(Entry(i.toFloat(),tempN001Data.get(i).toString().toFloat()))
                    }
                    val tempN002Data = temperatureArray.getJSONObject(1).getJSONArray("data")
                    for (i in 0 until tempN002Data.length()) {
                        temperatureN002.add(Entry(i.toFloat(), tempN002Data.get(i).toString().toFloat()))
                    }


                    val temperatureN001DataSet = LineDataSet(temperatureN001, "N001")
                    temperatureN001DataSet.notifyDataSetChanged()
                    temperatureN001DataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
                    temperatureN001DataSet.lineWidth = 1.3f
                    temperatureN001DataSet.color = Color.GREEN

                    val temperatureN002DataSet = LineDataSet(temperatureN002, "N002")
                    temperatureN002DataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
                    temperatureN002DataSet.notifyDataSetChanged()
                    temperatureN002DataSet.lineWidth = 1.3f
                    temperatureN002DataSet.color = Color.BLUE

                    val temperatureLegend = binding.temperature.legend
                    temperatureLegend.isEnabled = true

                    binding.temperature.xAxis.position = XAxis.XAxisPosition.BOTTOM
                    binding.temperature.description.isEnabled = false
                    binding.temperature.data = LineData(temperatureN001DataSet, temperatureN002DataSet)
                    binding.temperature.notifyDataSetChanged()
                    binding.temperature.invalidate()
                    binding.temperature.animateXY(100, 500)

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                TODO("Not yet implemented")
            }
        })
    }
}