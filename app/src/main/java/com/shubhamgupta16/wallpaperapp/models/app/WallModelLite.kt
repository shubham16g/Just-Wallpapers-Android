package com.shubhamgupta16.wallpaperapp.models.app


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class WallModelLite(
    @SerializedName("color")
    var color: String,
    @SerializedName("urls")
    var urls: AdminUrls,
    @SerializedName("rotation")
    val rotation: Int?
):Serializable