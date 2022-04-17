package com.shubhamgupta16.wallpaperapp.models.wallpapers


import com.shubhamgupta16.wallpaperapp.models.wallpapers.wall.WallModel
import java.io.Serializable

data class WallModelListHolder(
    val list:List<WallModel>
):Serializable