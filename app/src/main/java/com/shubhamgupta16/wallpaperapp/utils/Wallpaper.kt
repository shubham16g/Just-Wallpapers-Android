package com.shubhamgupta16.wallpaperapp.utils

import android.app.Activity
import android.app.WallpaperManager
import android.content.Context
import android.graphics.Bitmap
import java.io.IOException


fun Context.setWallpaper(bmap2: Bitmap, width: Int, height: Int) {

    val bitmap = Bitmap.createScaledBitmap(bmap2, width, height, true)
    val wallpaperManager = WallpaperManager.getInstance(this)
    try {
        wallpaperManager.setBitmap(bitmap)
    } catch (e: IOException) {
        e.printStackTrace()
    }
}