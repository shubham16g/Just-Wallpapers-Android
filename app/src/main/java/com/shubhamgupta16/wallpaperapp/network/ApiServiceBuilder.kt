package com.shubhamgupta16.wallpaperapp.network

import android.content.Context
import android.util.Log
import com.shubhamgupta16.wallpaperapp.BuildConfig
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.IOException

object ApiServiceBuilder {

    fun <T> build(context:Context, cacheDirSpecifier:String, cacheSize:Long, service: Class<T>): T{
        val cache = try {
            val cacheDir = File(context.cacheDir, cacheDirSpecifier)
            Cache(cacheDir, cacheSize)
        } catch (e: IOException) {
            Log.e("TAG", "Couldn't create http cache because of IO problem.", e)
            null
        }
        val client = OkHttpClient.Builder()
            .cache(cache)
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .header("Accept", "application/json")
                    .header("Authorization", "Bearer ${BuildConfig.API_KEY}")
                    .build()
                chain.proceed(request)
            }
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(service)
    }
}