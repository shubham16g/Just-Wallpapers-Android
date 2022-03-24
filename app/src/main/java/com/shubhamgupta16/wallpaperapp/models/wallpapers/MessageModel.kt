package com.shubhamgupta16.wallpaperapp.models.wallpapers


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class MessageModel(
    @SerializedName("message")
    val message: String,
):Serializable