package com.shubhamgupta16.wallpaperapp.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.shubhamgupta16.wallpaperapp.models.init.CategoryModel

@Dao
interface CategoryDao {
    @Query("SELECT * FROM categories")
    suspend fun getAllCategories(): List<CategoryModel>

    @Query("DELETE FROM categories")
    suspend fun deleteAllCategories()

    @Insert
    suspend fun insertCategory(category: CategoryModel)
}