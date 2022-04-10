package com.shubhamgupta16.wallpaperapp.utils

import android.Manifest
import android.app.WallpaperManager
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.annotation.RequiresPermission
import androidx.core.app.ActivityCompat
import androidx.core.graphics.drawable.toBitmap
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.IOException
import javax.inject.Inject

class CurrentWallpaper {
    var bitmap: Bitmap? = null
    var description: CharSequence? = null
    var icon: Drawable? = null

    constructor(bitmap: Bitmap) {
        this.bitmap = bitmap
    }

    constructor(description: CharSequence, icon: Drawable) {
        this.description = description
        this.icon = icon
    }
}

class WallpaperHelper @Inject constructor(@ApplicationContext val context: Context) {
    private val wallpaperManager = WallpaperManager.getInstance(context)

    fun getCurrentWall(): CurrentWallpaper? {
        return if (
            ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
            == PackageManager.PERMISSION_GRANTED
        ) {
            loadCurrentWall()
        } else null
    }

    fun getLockScreenWall(): CurrentWallpaper? {
        return if (
            ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
            == PackageManager.PERMISSION_GRANTED
        ) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                val descriptor = wallpaperManager.getWallpaperFile(WallpaperManager.FLAG_LOCK)
                if (descriptor != null) CurrentWallpaper(
                    BitmapFactory.decodeFileDescriptor(
                        descriptor.fileDescriptor
                    )
                )
                else loadCurrentWall()
            } else
                loadCurrentWall()
        } else null
    }

    @RequiresPermission(value = "android.permission.READ_EXTERNAL_STORAGE")
    private fun loadCurrentWall(): CurrentWallpaper {
        if (wallpaperManager.wallpaperInfo != null)
            return CurrentWallpaper(
                wallpaperManager.wallpaperInfo.loadDescription(context.packageManager),
                wallpaperManager.wallpaperInfo.loadThumbnail(context.packageManager)
            )
        return CurrentWallpaper(wallpaperManager.drawable.toBitmap())
    }
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