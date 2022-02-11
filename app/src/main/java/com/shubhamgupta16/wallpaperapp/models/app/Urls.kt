package com.shubhamgupta16.wallpaperapp.models.app


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Urls(
    @SerializedName("full")
    val full: String,
    /*@SerializedName("raw")
    val raw: String,*/
    @SerializedName("regular")
    val regular: String?,
    @SerializedName("small")
    val small: String
): Serializable