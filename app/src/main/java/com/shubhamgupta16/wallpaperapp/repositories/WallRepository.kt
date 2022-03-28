package com.shubhamgupta16.wallpaperapp.repositories

import com.shubhamgupta16.wallpaperapp.models.roommodels.FavWallModel
import com.shubhamgupta16.wallpaperapp.models.wallpapers.WallModel
import com.shubhamgupta16.wallpaperapp.models.wallpapers.WallpaperPageModel
import com.shubhamgupta16.wallpaperapp.network.ApiResponse
import com.shubhamgupta16.wallpaperapp.network.ApiService
import com.shubhamgupta16.wallpaperapp.network.request.RequestIdModel
import com.shubhamgupta16.wallpaperapp.room.FavWallDao
import javax.inject.Inject

class WallRepository @Inject constructor(private val apiService: ApiService, private val  favDao: FavWallDao) {


    suspend fun downloadWallpaper(wallId: Int){
        ApiResponse.from { apiService.downloadWallpaper(wallId) }
    }

    suspend fun getWalls(
        page: Int = 1,
        perPage: Int? = null,
        s: String? = null,
        orderBy: String? = null,
        category: String? = null,
        color: String? = null,
    ) = ApiResponse.from {
        apiService.getWalls(page, perPage, s, orderBy, category, color)
    }.apply {
        this.body?.data?.let { filterFavorites(it) }
    }

    suspend fun getWallsWithIds(
        wallIds: List<Int>,
        page: Int = 1,
        perPage: Int? = null,
    ) = ApiResponse.from {
        apiService.getWallsWithIds(RequestIdModel(wallIds), page, perPage)
    }.apply {
        this.body?.data?.let { filterFavorites(it) }
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
        return getWallsWithIds(wallIds, page, perPage)
    }

    suspend fun removeFav(wallId: Int) {
        favDao.deleteFav(wallId)
    }

    suspend fun applyFav(wallId: Int) {
        favDao.insertFav(FavWallModel(wallId = wallId))
    }

    suspend fun filterFavorites(_list: List<WallModel?>) {
        for ((i, wall) in _list.withIndex()) {
            if (wall == null) continue
            _list[i]?.isFav = favDao.isFav(wall.wallId) != null
        }
    }
}