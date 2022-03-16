package com.kadek.gis.utils

import android.content.Context
import android.graphics.Bitmap
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

object Helper {
    fun getImage(context: Context, address: String) : Bitmap {

        val baseUrl = "https://mygis.my.id/storage/"

        return Glide.with(context)
            .asBitmap()
            .load(baseUrl + address)
            .apply(RequestOptions().override(200,200))
            .submit()
            .get()
    }
}