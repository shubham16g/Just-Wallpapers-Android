package com.shubhamgupta16.wallpaperapp.models.app


import com.google.gson.annotations.SerializedName

data class WallpaperPageModel(
    @SerializedName("current_page")
    val currentPage: Int,
    @SerializedName("data")
    val data: List<WallModelLite>,
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
)