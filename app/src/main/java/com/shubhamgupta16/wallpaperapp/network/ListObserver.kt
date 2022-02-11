package com.shubhamgupta16.wallpaperapp.network

data class ListObserver(
    val case: ListCase,
    val from: Int = -1,
    val itemCount: Int = -1,
    val at: Int = -1
)