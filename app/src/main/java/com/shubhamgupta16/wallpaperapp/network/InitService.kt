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

interface InitService {

    /*@GET("init")
    suspend fun getInitData(): Response<InitModel>*/

    @GET("list/color")
    suspend fun getColors(): Response<List<ColorModel>>

    @GET("list/category")
    suspend fun getCategories(): Response<List<CategoryModel>>
}