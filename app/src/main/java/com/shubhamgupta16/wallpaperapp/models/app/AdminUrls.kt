package com.shubhamgupta16.wallpaperapp.models.app


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class AdminUrls(
    @SerializedName("small")
    var small: String,
    @SerializedName("regular")
    var regular: String?,
):Serializable