package com.shubhamgupta16.wallpaperapp.repositories

import android.graphics.Bitmap
import com.shubhamgupta16.wallpaperapp.network.GlideService
import com.shubhamgupta16.wallpaperapp.utils.RotationTransform
import javax.inject.Inject

class GlideRepository @Inject constructor(private val glideService: GlideService) {


    suspend fun downloadImage(
        imageUrl: String,
        rotation: Int = 0,
    ): Bitmap? {
        return glideService.downloadBitmap(imageUrl) {
            transform(RotationTransform(rotation.toFloat()))
        }
    }
}