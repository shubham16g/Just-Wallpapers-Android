package com.shubhamgupta16.justwallpapers.models.request

import com.google.gson.annotations.SerializedName

data class RequestIdModel(
    @SerializedName("list")
    val list: List<Int>
)