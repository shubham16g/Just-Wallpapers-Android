package com.shubhamgupta16.wallpaperapp.repositories

import com.shubhamgupta16.wallpaperapp.models.roommodels.FavWallModel
import com.shubhamgupta16.wallpaperapp.models.wallpapers.WallpaperPageModel
import com.shubhamgupta16.wallpaperapp.network.ApiResponse
import com.shubhamgupta16.wallpaperapp.network.ApiService
import com.shubhamgupta16.wallpaperapp.network.request.RequestIdModel
import com.shubhamgupta16.wallpaperapp.room.FavWallDao
import javax.inject.Inject

class WallRepository @Inject constructor(private val apiService: ApiService, private val  favDao: FavWallDao) {

    suspend fun getWalls(
        page: Int = 1,
        perPage: Int? = null,
        s: String? = null,
        orderBy: String? = null,
        category: String? = null,
        color: String? = null,
    ): ApiResponse<WallpaperPageModel> {
        val response = apiService.getWalls(page, perPage, s, orderBy, category, color)
        if (response.isSuccessful) {
            return if (response.body() != null) {
                for ((i, wall) in response.body()!!.data.withIndex()) {
                    if (favDao.isFav(wall.wallId) != null)
                        response.body()!!.data[i].isFav = true
                }
                ApiResponse(response.body()!!)
            } else
                ApiResponse(500)
        }
        return ApiResponse(response.code())
    }

    suspend fun getFavoriteWallpapers(
        page: Int = 1,
        perPage: Int? = null
    ): ApiResponse<WallpaperPageModel> {
        val wallIds = favDao.getAllFavorites().map { it.wallId }
        if (wallIds.isEmpty()) return ApiResponse(
            WallpaperPageModel(
                1,
                ArrayList(),
                1,
                1,
                18,
                1,
                0
            )
        )
        val response = apiService.getWallsWithIds(
            RequestIdModel(wallIds),
            page,
            perPage
        )
        return if (response.isSuccessful) {
            if (response.body() != null) {
                for (i in response.body()!!.data.indices) {
                    response.body()!!.data[i].isFav = true
                }
                ApiResponse(response.body()!!)
            } else
                ApiResponse(500)
        } else {
            ApiResponse(response.code())
        }

    }

    suspend fun removeFav(wallId: Int) {
        favDao.deleteFav(wallId)
    }

    suspend fun applyFav(wallId: Int) {
        favDao.insertFav(FavWallModel(wallId = wallId))
    }
}