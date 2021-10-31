package com.kadek.gis.ui

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.kadek.gis.databinding.ActivitySettingBinding

class SettingActivity : AppCompatActivity() {

    companion object {
        const val PREFS_NAME = "setting_prefs"
        const val SHOW_PRECIPITATION = "SHOW_PRECIPITATION"
    }

    private lateinit var binding: ActivitySettingBinding
    private lateinit var pref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pref = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        binding.precipitation.isChecked = pref.getBoolean(SHOW_PRECIPITATION, false)
        binding.precipitation.setOnCheckedChangeListener { _, isChecked ->
            saveState(isChecked)
        }
        Log.d("setting preference", SHOW_PRECIPITATION)
    }
    private fun saveState(state: Boolean) {
        val editState = pref.edit()
        editState.putBoolean(SHOW_PRECIPITATION, state)
        editState.apply()
    }
}