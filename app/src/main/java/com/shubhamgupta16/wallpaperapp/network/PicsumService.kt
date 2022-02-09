package com.shubhamgupta16.wallpaperapp.network

import com.shubhamgupta16.wallpaperapp.models.picsum.PicsumModel
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.*

interface PicsumService {

    companion object {
        fun getInstance() : PicsumService {
            val retrofit = Retrofit.Builder()
                .baseUrl(Config.demoUrl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(PicsumService::class.java)
        }
    }



    @GET("list")
    suspend fun getImagesList(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 40,
    ): Response<List<PicsumModel>>
}