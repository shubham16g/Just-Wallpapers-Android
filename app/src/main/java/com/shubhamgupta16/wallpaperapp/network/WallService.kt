package com.shubhamgupta16.wallpaperapp.network

import com.shubhamgupta16.wallpaperapp.models.init.CategoryModel
import com.shubhamgupta16.wallpaperapp.models.init.ColorModel
import com.shubhamgupta16.wallpaperapp.models.wallpapers.WallpaperPageModel
import com.shubhamgupta16.wallpaperapp.network.request.RequestIdModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface WallService {

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
}