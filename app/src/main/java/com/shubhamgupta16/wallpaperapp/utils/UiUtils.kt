package com.shubhamgupta16.wallpaperapp.utils

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.TransitionDrawable
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.annotation.ColorInt
import androidx.core.graphics.ColorUtils
import androidx.core.text.buildSpannedString
import androidx.core.text.color
import androidx.core.view.*
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.textfield.TextInputLayout
import com.shubhamgupta16.wallpaperapp.R
import kotlin.math.roundToInt

val Int.dp get() = (this / (Resources.getSystem().displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)).roundToInt()
val Float.dp get() = this / (Resources.getSystem().displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)

val Int.px get() :Int = (this * Resources.getSystem().displayMetrics.density).roundToInt()
val Float.px get() = this * Resources.getSystem().displayMetrics.density


fun RequestBuilder<Bitmap>.addBitmapListener(
    listener: (isReady: Boolean, resource: Bitmap?, e: GlideException?) -> Unit
): RequestBuilder<Bitmap?> {
    return addListener(object : RequestListener<Bitmap> {
        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<Bitmap>?,
            isFirstResource: Boolean
        ): Boolean {
            listener(false, null, e)
            return false
        }

        override fun onResourceReady(
            resource: Bitmap?,
            model: Any?,
            target: Target<Bitmap>?,
            dataSource: DataSource?,
            isFirstResource: Boolean
        ): Boolean {
            listener(true, resource, null)
            return false
        }

    })
}

fun darkenColor(@ColorInt color: Int, alpha:Int = 24): Int {
    return ColorUtils.setAlphaComponent(Color.HSVToColor(FloatArray(3).apply {
        Color.colorToHSV(color, this)
        Log.d("TAG", "darkenColor: ${this[0]}, ${this[1]}, ${this[2]}")
        this[1] *= 0.4f
        this[2] = if (this[2] > 0.7f) 0.6f else 0.35f
        Log.d("TAG", "darkenColor: ${this[0]}, ${this[1]}, ${this[2]}")
        Log.d("TAG", "darkenColor: ======================")
    }), alpha)
}

fun ImageView.fadeImage(bitmap: Bitmap?) {
    val td = TransitionDrawable(
        arrayOf(
            drawable ?: ColorDrawable(Color.TRANSPARENT),
            BitmapDrawable(resources, bitmap)
        )
    )
    setImageDrawable(td)
    Handler(Looper.getMainLooper()).post {
        td.startTransition(200)
    }
}

fun Activity.setTransparentStatusBar() {
    WindowCompat.setDecorFitsSystemWindows(window, false)
    window.statusBarColor = Color.TRANSPARENT
    window.navigationBarColor = Color.parseColor("#01ffffff")
}

fun Activity.hideSystemUI() {
    WindowCompat.setDecorFitsSystemWindows(window, false)
    WindowInsetsControllerCompat(window, window.decorView).let { controller ->
        controller.hide(WindowInsetsCompat.Type.systemBars())
        controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_BARS_BY_SWIPE
    }
}

fun Activity.showSystemUI() {
    WindowCompat.setDecorFitsSystemWindows(window, false)
    WindowInsetsControllerCompat(window, window.decorView).show(WindowInsetsCompat.Type.systemBars())
}

fun Context.getNavigationBarHeight(): Int {
    val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
    return if (resourceId > 0) {
        resources.getDimensionPixelSize(resourceId)
    } else
        0
}
