package com.kadek.gis.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.kadek.gis.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({
            val moveToMainIntent = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(moveToMainIntent)
            finish()
        }, 1000)
    }
}