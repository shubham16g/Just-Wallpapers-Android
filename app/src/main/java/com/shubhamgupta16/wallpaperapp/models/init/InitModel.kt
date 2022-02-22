package com.shubhamgupta16.wallpaperapp.models.init

import com.google.gson.annotations.SerializedName

data class InitModel(
    @SerializedName("categories")
    val categories: List<CategoryModel>,
    @SerializedName("colors")
    val colors: List<ColorModel>
)