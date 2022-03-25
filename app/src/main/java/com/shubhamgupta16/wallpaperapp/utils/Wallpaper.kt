package com.shubhamgupta16.wallpaperapp.utils

import android.app.Activity
import android.app.WallpaperManager
import android.content.Context
import android.content.res.Configuration
import android.graphics.Bitmap
import android.os.Build
import android.widget.Toast
import com.bumptech.glide.Glide
import com.shubhamgupta16.wallpaperapp.models.wallpapers.WallModel
import java.io.IOException


fun Activity.fetchWallpaperBitmap(model: WallModel, listener:(bitmap: Bitmap?)->Unit){
    Glide.with(this).asBitmap().load(model.urls.regular)
        .transform(RotationTransform((model.rotation ?: 0).toFloat()))
        .addBitmapListener { isReady, resource, e ->
            runOnUiThread{
                listener(resource)
            }

        }.submit()
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