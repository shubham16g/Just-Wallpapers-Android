package com.shubhamgupta16.wallpaperapp.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.shubhamgupta16.wallpaperapp.models.init.CategoryModel
import com.shubhamgupta16.wallpaperapp.models.init.ColorModel
import com.shubhamgupta16.wallpaperapp.models.roommodels.FavWallModel
import com.shubhamgupta16.wallpaperapp.models.wallpapers.WallModel

@Dao
interface FavWallDao {
    @Query("SELECT * FROM favorites")
    suspend fun getAllFavorites(): List<FavWallModel>

    @Query("DELETE FROM favorites")
    suspend fun deleteAllFavorites()

    @Insert
    suspend fun insertFav(favWallModel: FavWallModel)

    @Delete
    suspend fun deleteFav(favWallModel: FavWallModel)
}