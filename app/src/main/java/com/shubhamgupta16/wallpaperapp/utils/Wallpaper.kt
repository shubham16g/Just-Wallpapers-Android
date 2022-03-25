package com.shubhamgupta16.wallpaperapp.utils

import android.app.Activity
import android.app.WallpaperManager
import android.content.Context
import android.content.res.Configuration
import android.graphics.Bitmap
import android.os.Build
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import java.io.IOException

class ImageFetcher(private val activity: Activity, url: String, rotation: Int = 0, useCache:Boolean=true) {
    init {
        Log.d("TAG", "URL: $url")
        Glide.with(activity).asBitmap().load(url)
            .transform(RotationTransform(rotation.toFloat()))
            .timeout(20000)
            .diskCacheStrategy(if (useCache) DiskCacheStrategy.AUTOMATIC else DiskCacheStrategy.NONE)
            .addBitmapListener { _, resource, e ->
                activity.runOnUiThread {
                    if (resource != null) {
                        successListener?.let { it1 -> it1(resource) }
                    } else {
                        errorListener?.let { it(e?.message ?: "Unknown Error") }
                    }
                }

            }.submit()
    }

    var successListener: ((bitmap: Bitmap) -> Unit)? = null
    var errorListener: ((message: String) -> Unit)? = null
    fun onSuccess(listener: (bitmap: Bitmap) -> Unit): ImageFetcher {
        successListener = listener
        return this
    }

    fun onError(listener: (message: String) -> Unit): ImageFetcher {
        errorListener = listener
        return this
    }
}

/*fun Activity.fetchWallpaperBitmap(model: WallModel, listener: (bitmap: Bitmap) -> Unit) {
    Glide.with(this).asBitmap().load(model.urls.regular)
        .transform(RotationTransform((model.rotation ?: 0).toFloat()))
        .addBitmapListener { isReady, resource, e ->
            resource?.let {
                runOnUiThread { listener(it) }
            }

        }.submit()
}*/

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