package com.shubhamgupta16.wallpaperapp.models.picsum


import android.util.Log
import androidx.core.net.toUri
import com.google.gson.annotations.SerializedName

data class PicsumModel(
    @SerializedName("author")
    val author: String,
    @SerializedName("download_url")
    val downloadUrl: String,
    @SerializedName("height")
    val height: Int,
    @SerializedName("id")
    val id: String,
    @SerializedName("url")
    val url: String,
    @SerializedName("width")
    val width: Int
){
    val thumbUrl :String
    get() {
        val uri= downloadUrl.toUri()
        val segments = uri.pathSegments
        return "https://${uri.host}/${segments[0]}/${segments[1]}/480/1000"
    }
}