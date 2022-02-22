package com.shubhamgupta16.wallpaperapp.models.init

import com.google.gson.annotations.SerializedName

data class CategoryModel(
    @SerializedName("name")
    val name: String,
    @SerializedName("popularity")
    val popularity: Int
)