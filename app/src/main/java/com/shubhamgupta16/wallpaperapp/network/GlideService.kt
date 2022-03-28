package com.shubhamgupta16.wallpaperapp.network

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject

class GlideService @Inject constructor(@ApplicationContext val context: Context) {

    suspend fun downloadBitmap(
        imageUrl: String, builder: RequestBuilder<Bitmap>.() -> RequestBuilder<Bitmap>
    ): Bitmap? {
        return suspendCancellableCoroutine { cont ->
            Glide.with(context)
                .asBitmap()
                .load(imageUrl)
                .builder()
                .listener(object : RequestListener<Bitmap> {
                    override fun onResourceReady(
                        bitmap: Bitmap?, model: Any?, target: Target<Bitmap>?,
                        dataSource: DataSource?, isFirstResource: Boolean
                    ): Boolean {
                        cont.resumeWith(Result.success(bitmap))
                        return false
                    }

                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Bitmap>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        cont.resumeWith(Result.success(null))
                        return false
                    }
                }).submit()
        }
    }
}