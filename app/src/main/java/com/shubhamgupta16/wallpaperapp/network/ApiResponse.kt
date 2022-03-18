package com.shubhamgupta16.wallpaperapp.network

class ApiResponse<T> {
    var data:T?=null
    var error:ApiError?=null

    constructor(data:T){
        this.data = data
    }
    constructor(errorCode:Int){
        error = ApiError(errorCode)
    }
}

