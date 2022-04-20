package com.shubhamgupta16.justwallpapers.models.wallpapers


import com.google.gson.annotations.SerializedName

data class FeaturedModel(
    @SerializedName("data")
    val data: WallModel,
    @SerializedName("sub_title")
    val subTitle: String?,
    @SerializedName("title")
    val title: String?
)
