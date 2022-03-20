package com.shubhamgupta16.wallpaperapp.utils

import android.app.WallpaperManager
import android.content.Context
import android.content.res.Configuration
import android.graphics.Bitmap
import android.os.Build
import android.widget.Toast
import com.bumptech.glide.Glide
import com.shubhamgupta16.wallpaperapp.models.wallpapers.WallModel
import java.io.IOException


fun Context.fetchAndApplyWallpaper(model: WallModel, flag: Int?=null){
    if (isOrientationLandscape()){
        Toast.makeText(this, "Set Wallpapers only in Portrait Mode", Toast.LENGTH_SHORT).show()
        return
    }
    Glide.with(this).asBitmap().load(model.urls.regular)
        .transform(RotationTransform((model.rotation ?: 0).toFloat()))
        .addBitmapListener { isReady, resource, e ->
            if (isReady)
                resource?.let { it1 ->
                    setWallpaper(
                        it1,
                        it1.width,
                        it1.height,
                        flag
                    )
                }
        }.submit()
    Toast.makeText(this, "Loading...", Toast.LENGTH_SHORT).show()
    Toast.makeText(this, "Wallpaper Set Successfully!", Toast.LENGTH_SHORT).show()
}

fun Context.setWallpaper(bmap2: Bitmap, width: Int, height: Int, flag: Int? = null) {
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