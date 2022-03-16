package com.shubhamgupta16.wallpaperapp.repositories

import com.shubhamgupta16.wallpaperapp.models.roommodels.FavWallModel
import com.shubhamgupta16.wallpaperapp.models.wallpapers.WallpaperPageModel
import com.shubhamgupta16.wallpaperapp.network.ApiService
import com.shubhamgupta16.wallpaperapp.room.FavWallDao
import retrofit2.Response
import javax.inject.Inject

class WallRepository @Inject constructor(private val apiService: ApiService, private val  favDao: FavWallDao) {

    suspend fun getWalls(
        page: Int = 1,
        perPage: Int? = null,
        s: String? = null,
        orderBy: String? = null,
        category: String? = null,
        color: String? = null,
    ): Response<WallpaperPageModel> {
        val response = apiService.getWalls(page, perPage, s, orderBy, category, color)
        if (response.isSuccessful && response.body() != null){
            response.body()?.let {
                for ((i, wall) in it.data.withIndex()) {
                    if (favDao.isFav(wall.wallId) != null)
                        it.data[i].isFav = true
                }
            }
        }
        return response
    }

    suspend fun removeFav(wallId:Int){
        favDao.deleteFav(wallId)
    }

    suspend fun applyFav(wallId:Int){
        favDao.insertFav(FavWallModel(wallId = wallId))
    }
}