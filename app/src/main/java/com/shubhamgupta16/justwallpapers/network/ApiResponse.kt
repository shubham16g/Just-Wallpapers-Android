package com.shubhamgupta16.justwallpapers.network

import android.util.Log
import retrofit2.Response
import java.io.IOException

class ApiResponse<T> {
    val isSuccessful: Boolean get() = code in 200..299
    var body: T? = null

    private var _code: Int
    val code: Int get() = _code

    private var msg: String? = null
    val message: String
        get() = msg ?: when (code) {
            404 -> "Not Found"
            else -> "Unknown Error Occurred"
        }

    constructor(data: T, code: Int = 200, message: String? = null) {
        this.body = data
        this.msg = message
        this._code = code
    }

    constructor(code: Int, message: String? = null) {
        this._code = code
        this.msg = message
    }

    companion object {
        suspend fun<T> from(caller: suspend ()->Response<T>): ApiResponse<T>{
            return try {
                val response = caller()
                Log.d("TAG", "from: $response")
                return if (response.isSuccessful) {
                    if (response.body() != null) {
                        ApiResponse(response.body()!!)
                    } else
                        ApiResponse(500)
                } else {
                    ApiResponse(response.code())
                }
            }catch (e: IOException){
                Log.d("Repo", "error: ${e.message}")
                ApiResponse(800, e.message)
            }
        }
    }
}

