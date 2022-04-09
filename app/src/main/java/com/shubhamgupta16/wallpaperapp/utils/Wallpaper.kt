package com.shubhamgupta16.wallpaperapp.utils

import android.Manifest
import android.app.WallpaperManager
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.core.app.ActivityCompat
import java.io.IOException

fun Context.getCurrentWall(): Drawable? {
    val wallpaperManager = WallpaperManager.getInstance(this);
    return if (
        ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
        == PackageManager.PERMISSION_GRANTED
    ) wallpaperManager.drawable
    else null
}

fun Context.applyWall(bmap2: Bitmap, width: Int, height: Int, flag: Int? = null) {
    val bitmap = bmap2
//    val bitmap = Bitmap.createScaledBitmap(bmap2, width, height, true)
    val wallpaperManager = WallpaperManager.getInstance(this)
    try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (flag != null)
                wallpaperManager.setBitmap(bitmap, null, true, flag)
            else
                wallpaperManager.setBitmap(bitmap, null, true)
        } else {
            wallpaperManager.setBitmap(bitmap)
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }
}

fun Context.isOrientationLandscape(): Boolean {
    val orientation: Int = resources.configuration.orientation
    return orientation == Configuration.ORIENTATION_LANDSCAPE
}