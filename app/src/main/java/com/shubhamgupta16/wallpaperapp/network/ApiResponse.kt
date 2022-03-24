package com.shubhamgupta16.wallpaperapp.network

class ApiResponse<T> {
    val isSuccessful: Boolean get() = code in 200..299
    var data: T? = null

    private var _code: Int
    val code: Int get() = _code

    private var msg: String? = null
    val message: String
        get() = msg ?: when (code) {
            404 -> "Not Found"
            else -> "Unknown Error Occurred"
        }

    constructor(data: T, code: Int = 200, message: String? = null) {
        this.data = data
        this._code = code
    }

    constructor(code: Int, message: String? = null) {
        this._code = code
        this.msg = message
    }
}

