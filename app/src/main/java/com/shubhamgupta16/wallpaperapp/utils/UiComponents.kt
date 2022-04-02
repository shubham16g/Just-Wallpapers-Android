package com.shubhamgupta16.wallpaperapp.utils

import android.app.AlertDialog
import android.content.Context
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.shubhamgupta16.wallpaperapp.R

fun Context.bottomSheet(
    binding: ViewBinding,
    cancelable: Boolean = true
): BottomSheetDialog {
    val dialog = BottomSheetDialog(this, R.style.BottomSheetDialog) // Style here
    dialog.setCancelable(cancelable)
    dialog.setContentView(binding.root)
    return dialog
}

fun Context.alertDialog(binding: ViewBinding, cancelable: Boolean = true) =
    AlertDialog.Builder(this, R.style.DefaultAlertTheme).setView(binding.root).setCancelable(cancelable).create()


/*
    fun Activity.applyBlurView(blurView: BlurView, radius: Float) {
        val decorView: View = window.decorView
        val windowBackground: Drawable = decorView.background
        blurView.setupWith(decorView.findViewById(android.R.id.content))
            .setFrameClearDrawable(windowBackground)
            .setBlurAlgorithm(RenderScriptBlur(this))
            .setBlurRadius(radius)
            .setBlurAutoUpdate(true)
            .setHasFixedTransformationMatrix(true);
        blurView.outlineProvider = ViewOutlineProvider.BACKGROUND;
        blurView.clipToOutline = true;
    }
*/