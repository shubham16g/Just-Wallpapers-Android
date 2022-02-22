package com.shubhamgupta16.wallpaperapp.models.wallpapers


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Author(
    @SerializedName("author_id")
    val authorId: Int,
    @SerializedName("user_name")
    val userName: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String?,
    @SerializedName("image")
    val image: String?,
):Serializable