package com.shubhamgupta16.justwallpapers.utils

import android.Manifest
import android.app.WallpaperManager
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.media.ThumbnailUtils
import android.os.Build
import androidx.annotation.RequiresPermission
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.shubhamgupta16.justwallpapers.R
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.IOException
import javax.inject.Inject
import kotlin.math.roundToInt

class CurrentWallpaper {
    var bitmap: Bitmap? = null
    var description: CharSequence? = null
    var icon: Drawable? = null

    constructor(bitmap: Bitmap) {
        this.bitmap = bitmap
    }

    constructor(description: CharSequence, icon: Drawable?) {
        this.description = description
        this.icon = icon
    }
}

fun Context.applyWall(bmap2: Bitmap, flag: Int? = null, screenCropped: Boolean = false) {
    val wallpaperManager = WallpaperManager.getInstance(this)
    val screenHeight = getScreenHeight()
    val screenWidth = getScreenWidth()
    val ratio: Float = screenHeight / bmap2.height.toFloat() // 16/9 which is grater then 1
    val h = if (bmap2.height > screenHeight) screenHeight else bmap2.height
    val w = if (bmap2.height > screenHeight) (bmap2.width * ratio).roundToInt() else bmap2.width

    val scaledBitmap = Bitmap.createScaledBitmap(bmap2, w, h, true)
    val bitmap =
        if (screenCropped) ThumbnailUtils.extractThumbnail(
            scaledBitmap,
            screenWidth,
            screenHeight
        )
        else scaledBitmap
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
