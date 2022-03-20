package com.shubhamgupta16.wallpaperapp.network

import android.content.Context
import com.shubhamgupta16.wallpaperapp.BuildConfig
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

object ApiServiceBuilder {

    fun <T> build(context:Context, cacheDirSpecifier:String, cacheSize:Long, service: Class<T>): T{
        val cacheDir = File(context.cacheDir, "wall-cache")
        val cache = Cache(cacheDir, 10 * 1024 * 1024)
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