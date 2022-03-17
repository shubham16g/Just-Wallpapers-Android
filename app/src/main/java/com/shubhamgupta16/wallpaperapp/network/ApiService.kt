package com.shubhamgupta16.wallpaperapp.network

import android.content.Context
import com.shubhamgupta16.wallpaperapp.BuildConfig
import com.shubhamgupta16.wallpaperapp.models.init.InitModel
import com.shubhamgupta16.wallpaperapp.models.wallpapers.WallpaperPageModel
import com.shubhamgupta16.wallpaperapp.network.request.RequestIdModel
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import java.io.File

interface ApiService {

    companion object {
        fun getInstance(@ApplicationContext context: Context): ApiService {
            val httpCacheDirectory = File(context.cacheDir, "response_cache")
            val cacheSize: Long = 10 * 1024 * 1024 // 10 MiB
            val cache = Cache(httpCacheDirectory, cacheSize)

            val client = OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val request = chain.request().newBuilder()
                        .header("Accept", "application/json")
                        .header("Authorization", "Bearer ${BuildConfig.API_KEY}")
                        .build()
                    chain.proceed(request)
                }
                .cache(cache)
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            return retrofit.create(ApiService::class.java)
        }
    }

    @GET("wall")
    suspend fun getWalls(
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int? = null,
        @Query("s") s: String? = null,
        @Query("order_by") orderBy: String? = null,
        @Query("category") category: String? = null,
        @Query("color") color: String? = null,
    ): Response<WallpaperPageModel>

    @POST("wall/list")
    suspend fun getWallsWithIds(
        @Body list: RequestIdModel,
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int? = null,
    ): Response<WallpaperPageModel>

    @GET("init")
    suspend fun getInitData(): Response<InitModel>
}