package com.shubhamgupta16.wallpaperapp.models.init

import com.google.gson.annotations.SerializedName

data class ColorModel(
    @SerializedName("name")
    val name: String,
    @SerializedName("popularity")
    val popularity: Int,
    @SerializedName("value")
    val value: String,
)