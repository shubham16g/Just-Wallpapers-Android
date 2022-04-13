package com.shubhamgupta16.wallpaperapp.models.wallpapers


import com.google.gson.annotations.SerializedName
import com.shubhamgupta16.wallpaperapp.models.ad.AdModel
import java.io.Serializable

data class WallModel(
    @SerializedName("wall_id")
    val wallId: Int,
    @SerializedName("source")
    val source: String,
    @SerializedName("color")
    val color: String,
    @SerializedName("urls")
    val urls: Urls,
    @SerializedName("categories")
    val categories: List<String>,
    @SerializedName("colors")
    val colors: List<String>,
    @SerializedName("tags")
    val tags: List<String>,
    @SerializedName("downloads")
    val downloads: Int,
    @SerializedName("coins")
    val coins: Int,
    @SerializedName("author")
    val author: Author?,
    @SerializedName("flip")
    val flip: String?,
    @SerializedName("license")
    val license: String?,
    @SerializedName("rotation")
    val rotation: Int?,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    var isFav: Boolean = false
): AdModel(), Serializable