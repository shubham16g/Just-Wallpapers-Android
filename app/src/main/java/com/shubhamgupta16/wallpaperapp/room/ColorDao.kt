package com.shubhamgupta16.wallpaperapp.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.shubhamgupta16.wallpaperapp.models.init.ColorModel

@Dao
interface ColorDao {
    @Query("SELECT * FROM colors")
    suspend fun getAllColors(): List<ColorModel>

    @Query("DELETE FROM colors")
    suspend fun deleteAllColors()

    @Insert
    suspend fun insertColor(colorModel: ColorModel)
}