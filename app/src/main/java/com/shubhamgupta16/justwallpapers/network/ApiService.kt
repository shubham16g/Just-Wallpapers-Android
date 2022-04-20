package com.shubhamgupta16.justwallpapers.network

import android.content.Context
import com.shubhamgupta16.justwallpapers.models.init.BaseModel
import com.shubhamgupta16.justwallpapers.models.init.CategoryModel
import com.shubhamgupta16.justwallpapers.models.init.ColorModel
import com.shubhamgupta16.justwallpapers.models.wallpapers.FeaturedModel
import com.shubhamgupta16.justwallpapers.models.wallpapers.MessageModel
import com.shubhamgupta16.justwallpapers.models.wallpapers.WallpaperPageModel
import com.shubhamgupta16.justwallpapers.models.request.RequestIdModel
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @GET("wall")
    suspend fun getWalls(
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int? = null,
        @Query("s") s: String? = null,
        @Query("order_by") orderBy: String? = null,
        @Query("category") category: String? = null,
        @Query("color") color: String? = null,
    ): Response<WallpaperPageModel>

    @GET("wall/download/{id}")
    suspend fun downloadWallpaper(
        @Path("id") wallId:Int
    ): Response<MessageModel>

    @POST("wall/list")
    suspend fun getWallsWithIds(
        @Body list: RequestIdModel,
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int? = null,
    ): Response<WallpaperPageModel>

    @GET("list/color")
    suspend fun getColors(): Response<List<ColorModel>>

    @GET("list/category")
    suspend fun getCategories(): Response<List<CategoryModel>>

    @GET("base")
    suspend fun getBase(): Response<BaseModel>

    companion object {
        fun build(context: Context) =
            ApiServiceBuilder.build(context, ApiService::class.java)
    }
}