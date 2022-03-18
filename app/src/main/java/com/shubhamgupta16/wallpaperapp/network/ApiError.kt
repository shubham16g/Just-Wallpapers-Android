package com.shubhamgupta16.wallpaperapp.network

data class ApiError(val code:Int){
    val message:String
    get() = when(code){
        404->"Not Found"
        else-> "Unknown Error Occurred"
    }
}