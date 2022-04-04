package com.shubhamgupta16.wallpaperapp.models.wallpapers


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class WallpaperFeaturedModel(
    @SerializedName("data")
    val data: List<WallModel>,
    @SerializedName("titles")
    val titles: List<String>,
):Serializable