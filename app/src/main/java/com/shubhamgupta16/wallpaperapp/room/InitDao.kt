package com.shubhamgupta16.wallpaperapp.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.shubhamgupta16.wallpaperapp.models.init.CategoryModel
import com.shubhamgupta16.wallpaperapp.models.init.ColorModel

@Dao
interface InitDao {
    /*@Query("SELECT * FROM products WHERE name LIKE '%' || :path || '%' ORDER BY (CASE WHEN name = :path THEN 1 WHEN name LIKE :path || '%' THEN 2 ELSE 3 END),name LIMIT 10")
    fun searchProducts(path: String):LiveData<List<ProductModel>>*/

    @Query("SELECT * FROM categories")
    fun getAllCategories(): LiveData<List<CategoryModel>>


    @Query("SELECT * FROM colors")
    fun getAllColors(): LiveData<List<ColorModel>>

    @Query("DELETE FROM categories")
    suspend fun deleteAllCategories()

    @Query("DELETE FROM colors")
    suspend fun deleteAllColors()

    @Insert
    suspend fun insertCategory(category: CategoryModel)

    @Insert
    suspend fun colorModel(colorModel: ColorModel)
}