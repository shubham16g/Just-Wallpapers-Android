package com.shubhamgupta16.wallpaperapp.models.app


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class WallModelList(
    val list:List<WallModelLite>
):Serializable