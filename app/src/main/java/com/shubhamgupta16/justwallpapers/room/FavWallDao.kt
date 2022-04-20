package com.shubhamgupta16.justwallpapers.room

import androidx.room.*
import com.shubhamgupta16.justwallpapers.models.roommodels.FavWallModel

@Dao
interface FavWallDao {
    @Query("SELECT * FROM favorites")
    suspend fun getAllFavorites(): List<FavWallModel>

    @Query("DELETE FROM favorites")
    suspend fun deleteAllFavorites()

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFav(favWallModel: FavWallModel)

    @Query("SELECT * FROM favorites WHERE wallId = :wallId LIMIT 1")
    suspend fun isFav(wallId: Int): FavWallModel?

    @Query("DELETE FROM favorites WHERE wallId = :wallId")
    suspend fun deleteFav(wallId: Int)
}