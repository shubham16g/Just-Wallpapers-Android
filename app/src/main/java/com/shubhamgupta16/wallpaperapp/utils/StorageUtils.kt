package com.shubhamgupta16.wallpaperapp.utils

import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream

/** Permissions */

fun ComponentActivity.getPermissionLauncher(listener: (isAllPermissionGranted:Boolean, Map<String,Boolean>)->Unit): ActivityResultLauncher<Array<String>> {
    return registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
        listener(it.values.all { value-> value }, it)
    }
}

fun Fragment.getPermissionLauncher(listener: (isAllPermissionGranted:Boolean, Map<String,Boolean>)->Unit): ActivityResultLauncher<Array<String>> {
    return registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
        listener(it.values.all { value-> value }, it)
    }
}

fun ActivityResultLauncher<Array<String>>.launchPermission(
    context: Context,
    read: Boolean = false,
    write: Boolean = false,
    extraPermissions:Array<String>? = null
) {
    val permissionToRequest = mutableListOf<String>()
    if (!context.isHaveWriteExternalStoragePermission() && write)
        permissionToRequest.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
    if (!context.isHaveReadExternalStoragePermission() && read)
        permissionToRequest.add(android.Manifest.permission.READ_EXTERNAL_STORAGE)
        extraPermissions?.forEach {
            if (it != android.Manifest.permission.READ_EXTERNAL_STORAGE || it != android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            permissionToRequest.add(it)
        }
    if (permissionToRequest.isNotEmpty())
        launch(permissionToRequest.toTypedArray())
}


fun Context.isHaveReadExternalStoragePermission() =
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
            checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) ==
            PackageManager.PERMISSION_GRANTED

fun Context.isHaveWriteExternalStoragePermissionForced() =
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
            checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
            PackageManager.PERMISSION_GRANTED

fun Context.isHaveWriteExternalStoragePermission() =
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q || isHaveWriteExternalStoragePermissionForced()

enum class ImageFormat {
    JPEG, PNG
}

private fun getImageMimeType(format: ImageFormat) = when (format) {
    ImageFormat.JPEG -> "image/jpeg"
    ImageFormat.PNG -> "image/png"
}

private fun getImageExtension(format: ImageFormat) = when (format) {
    ImageFormat.JPEG -> ".jpg"
    ImageFormat.PNG -> ".png"
}

fun Context.saveImageToExternal(
    appFolder: String,
    imgName: String,
    bm: Bitmap,
    format: ImageFormat = ImageFormat.PNG
): Boolean {
    val outStream: OutputStream? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, imgName)
            put(MediaStore.MediaColumns.MIME_TYPE, getImageMimeType(format))
            put(
                MediaStore.MediaColumns.RELATIVE_PATH,
                "${Environment.DIRECTORY_PICTURES}/$appFolder"
            )
        }
        try {
            contentResolver.insert(
                MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY),
                contentValues
            )?.let { uri ->
                contentResolver.openOutputStream(uri)
            } ?: throw IOException("Unable to create Media Store entry")
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }

    } else {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            null
        } else {
            @Suppress("DEPRECATION")
            val imagesDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/$appFolder")
            imagesDir.mkdirs()
            val image = File(imagesDir, "$imgName${getImageExtension(format)}")
            FileOutputStream(image)
        }
    }
    return try {
        if (outStream == null) false
        else {
            outStream.use { stream ->
                if (!bm.compress(Bitmap.CompressFormat.JPEG, 95, stream))
                    throw IOException("Can't save bitmap")
            }

            true
        }
    } catch (e: IOException) {
        e.printStackTrace()
        false
    }
}
