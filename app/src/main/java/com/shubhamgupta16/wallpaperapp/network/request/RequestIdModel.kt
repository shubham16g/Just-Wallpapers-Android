package com.shubhamgupta16.wallpaperapp.network.request

import com.google.gson.annotations.SerializedName

data class RequestIdModel(
    @SerializedName("list")
    val list: ArrayList<Int>
)