package com.shubhamgupta16.justwallpapers.models.init


import com.google.gson.annotations.SerializedName
import com.shubhamgupta16.justwallpapers.models.wallpapers.WallModel
import java.io.Serializable

data class BaseModel(
    @SerializedName("featured")
    val featured: WallModel,
    @SerializedName("featured_title")
    val featuredTitle: String?,
    @SerializedName("featured_description")
    val featuredDescription: String?,
    @SerializedName("current_version")
    val currentVersion: Int,
    @SerializedName("immediate_update")
    val immediateUpdate: Int?,
    @SerializedName("play_store_url_short")
    val playStoreUrlShort: String?
):Serializable