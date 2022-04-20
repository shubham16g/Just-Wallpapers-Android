package com.shubhamgupta16.justwallpapers.models.wallpapers


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class WallpaperPageModel(
    @SerializedName("current_page")
    val currentPage: Int,
    @SerializedName("data")
    val data: List<WallModel>,
    @SerializedName("from")
    val from: Int,
    @SerializedName("last_page")
    val lastPage: Int,
    @SerializedName("per_page")
    val perPage: Int,
    @SerializedName("to")
    val to: Int,
    @SerializedName("total")
    val total: Int
):Serializable